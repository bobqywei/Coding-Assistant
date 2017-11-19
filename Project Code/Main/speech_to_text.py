import pyaudio
import wave
import math
import audioop
import time
from collections import deque

import json
from watson_developer_cloud import SpeechToTextV1

IBM_USERNAME = "dced0ac1-c955-4c30-90f4-05db435f7c6f"
IBM_PASSWORD = "GLnzMJg7E4M6"

stt = SpeechToTextV1(username=IBM_USERNAME, password=IBM_PASSWORD)

CHUNK = 1024
FORMAT = pyaudio.paInt16
CHANNELS = 1
RATE = 44100
WAVE_OUTPUT_FILENAME = "output.wav"


def get_idle_intensity():
	p = pyaudio.PyAudio()

	# Number of samples to be taken
	samples = 50
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


def speech_to_text(file):
	with open(file, "rb") as audio_content:
		# obtains response from api call (JSON to string)
		response = json.dumps(stt.recognize(audio_content, content_type="audio/wav"))
		# parses string to obtain just the transcript
		transcript = (response.split("transcript\": ")[1]).split("}")[0]

		return transcript


def listen(idle):
	p = pyaudio.PyAudio()
	stream = p.open(format=FORMAT, channels=CHANNELS, rate=RATE, input=True, frames_per_buffer=CHUNK)

	# stores the final recording
	recording = []

	# stores all unfiltered incoming audio
	audio_in = deque(maxlen=1 * int(RATE/CHUNK))
	# initial overlap compensates for a late recording by combining 5 secs prior of audio
	initial_overlap = deque(maxlen=int(0.5 * RATE/CHUNK))
	started = False;

	# Number of iterations to be run
	iters = 1

	while iters > 0:
		current_data = stream.read(CHUNK)
		audio_in.append(math.sqrt(abs(audioop.avg(current_data, 4))))

		# waits for intensity of audio to exceed idle intensity
		if sum([i > idle for i in audio_in]) > 0:
			if not started:
				print("Starting Recording")
				started = True
			recording.append(current_data)

		elif started:
			print("Finished Recording")

			# saves audio to .wav file
			# includes the initial overlap audio to compensate for late reaction timing
			file = save_to_wav(list(initial_overlap) + recording, p)

			stream.close()
			p.terminate()

			# converts .wav to text through call to Watson API
			response = speech_to_text(file)
			print(response)
			return response



if __name__ == '__main__':
	#IDLE = get_idle_intensity()
	listen(4000)
