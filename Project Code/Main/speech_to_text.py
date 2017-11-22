import pyaudio
import wave
import math
import audioop
import time
import text_to_speech as tts
import os
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
	wf.close()

	return filename + '.wav'


def speech_to_text(file):
	with open(file, "rb") as audio_content:

		# obtains response from api call (JSON to string)
		response = json.loads(json.dumps(stt.recognize(audio_content, content_type="audio/wav")))

		transcript = ""

		if len(response['results']) > 0:
			response = response['results'][0]['alternatives'][0]
			transcript = (response['transcript'])
			print(transcript)

		return transcript


def save_transcript(transcript, file):
	with open(file, "w") as save:
		save.write(transcript)


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

	iterations = 1

	while iterations > 0:

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

			iterations -= 1

			# saves audio to .wav file
			# includes the initial overlap audio to compensate for late reaction timing
			file = save_to_wav(list(initial_overlap) + recording, p)

			# closes the stream
			stream.close()
			p.terminate()

			# converts .wav to text through call to Watson API
			response = speech_to_text(file)

			if response:
				response.lstrip().rstrip().lower()
				save_transcript(response, os.getcwd() + "/Files/Transcript.txt")
			else:
				tts.tts_and_play("Sorry, I did not get that")

			# directory of temporary audio file
			file_dir = os.path.join(os.getcwd(), file)
			# deletes the temporary audio file after a response is obtained
			if os.path.isfile(file_dir):
				os.unlink(file_dir)

			return response

		else:
			initial_overlap.append(current_data)

if __name__ == '__main__':
	while True:
		response = listen(2500)

		if response:
			print(response)

			if response.startswith("goodbye"):
				print("OK. I'm going back to sleep.")
				break
