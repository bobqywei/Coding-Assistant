import pyaudio
import wave
import math
import audioop
import time
import os
from collections import deque


# Imports the Google Cloud client library
from google.cloud import speech
from google.cloud.speech import enums
from google.cloud.speech import types

from oauth2client.service_account import ServiceAccountCredentials

scopes = ['https://www.googleapis.com/auth/sqlservice.admin']

DIRNAME = os.path.dirname(__file__)
credentials = ServiceAccountCredentials.from_json_keyfile_name(
    os.path.join(DIRNAME, 'apikey.json'),
    scopes=scopes
)

client = speech.SpeechClient(credentials=credentials)

CHUNK = 1024
FORMAT = pyaudio.paInt16
CHANNELS = 1
RATE = 44100
WAVE_OUTPUT_FILENAME = "output.wav"


def get_idle_intensity():
	p = pyaudio.PyAudio()
	samples = 50 # Number of samples to be taken

	stream = p.open(format=FORMAT, channels=CHANNELS, rate=RATE, input=True, frames_per_buffer=CHUNK)

	# Obtains frequency intensities
	values = []
	for i in range(samples):
		values.append(math.sqrt(abs(audioop.avg(stream.read(CHUNK), 4))))

	# Sorts in descending order so that lowest intensities are first
	values = sorted(values, reverse=True);
	# idle intensity is the average of the lowest 20% of intensities
	idle_int = sum(values[:int(samples * 0.2)]) / int(samples * 0.2)

	print("Idle Intensity: ", idle_int)
	stream.close()
	p.terminate()

	return idle_int


def save_to_wav (data, p):
	filename = "record_" + str(int(time.time()))
	wf = wave.open(filename + '.wav', 'wb')
	wf.setnchannels(CHANNELS)
	wf.setsampwidth(p.get_sample_size(FORMAT))
	wf.setframerate(RATE)
	wf.writeframes(b''.join(data))
	print("Audio Recording Saved")
	wf.close()

	return filename + '.wav'


def speech_to_text(data):
	audio_content = b''.join(data)
	audio = types.RecognitionAudio(content=audio_content)

	config = types.RecognitionConfig(encoding=enums.RecognitionConfig.AudioEncoding.LINEAR16,
									 sample_rate_hertz=16000,
									 language_code='en-US')

	response = client.recognize(config, audio)

	for result in response.results:
		print(result.alternatives[0].transcript)


def listen(idle):
	p = pyaudio.PyAudio()

	stream = p.open(format=FORMAT, channels=CHANNELS, rate=RATE, input=True, frames_per_buffer=CHUNK)

	recording = []

	audio_in = deque(maxlen= 1 * int(RATE/CHUNK))
	initial_overlap = deque(maxlen= int(0.5 * RATE/CHUNK))
	started = False;
	iters = 50

	while iters > 0:
		current_data = stream.read(CHUNK)
		audio_in.append(math.sqrt(abs(audioop.avg(current_data, 4))))

		if sum([i > idle for i in audio_in]) > 0:
			if not started:
				print("Starting Recording")
				started = True
			recording.append(current_data)

		elif started:
			print("Finished Recording")

			#save_to_wav(list(initial_overlap) + recording, p)

			response = speech_to_text(list(initial_overlap) + recording)

			started = False
			audio_in = deque(maxlen= int(1 * RATE / CHUNK))
			initial_overlap = deque(maxlen= int(0.5 * RATE / CHUNK))
			recording = []
			iters -= 1

		else:
			initial_overlap.append(current_data)

	stream.close()
	p.terminate()


if __name__ == '__main__':
	IDLE = get_idle_intensity()
	listen(IDLE)