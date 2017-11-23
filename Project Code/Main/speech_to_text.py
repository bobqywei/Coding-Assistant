"""
	File containing functions necessary for audio recording and speech to text.
"""
# Imports needed files
import text_to_speech as tts

# Imports needed modules
import pyaudio
import wave
import math
import audioop
import time
import os
from collections import deque
import json
from watson_developer_cloud import SpeechToTextV1

# IBM Speech to Text credentials
IBM_USERNAME = "b78dc902-1d89-4c82-be62-5d8d19170d0a"
IBM_PASSWORD = "U1RkyDhxe8Gm"

# instance of speech to text module
stt = SpeechToTextV1(username=IBM_USERNAME, password=IBM_PASSWORD)

# audio input formatting
CHUNK = 1024
FORMAT = pyaudio.paInt16
CHANNELS = 1
RATE = 44100
WAVE_OUTPUT_FILENAME = "output.wav"


# Function obtains background audio intensity at idle
def get_idle_intensity():

	p = pyaudio.PyAudio()
	stream = p.open(format=FORMAT, channels=CHANNELS, rate=RATE, input=True, frames_per_buffer=CHUNK)

	# Number of samples to be taken
	samples = 50
	
	# Obtains and stores frequency intensities
	values = []
	for i in range(samples):
		values.append(math.sqrt(abs(audioop.avg(stream.read(CHUNK), 4))))

	# Sorts in descending order so that lowest intensities are first
	values = sorted(values, reverse=True);

	# idle intensity is the average of the lowest 20% of intensities
	idle_int = sum(values[:int(samples * 0.2)]) / int(samples * 0.2)

	# closes input stream
	stream.close()
	p.terminate()

	# returns the idle intensity
	return idle_int


# Function saves audio input to local file
def save_to_wav(data, p):
	# temporary file name
	filename = "record_" + str(int(time.time()))

	# sets up and writes data to file
	wf = wave.open(filename + '.wav', 'wb')
	wf.setnchannels(CHANNELS)
	wf.setsampwidth(p.get_sample_size(FORMAT))
	wf.setframerate(RATE)
	wf.writeframes(b''.join(data))
	wf.close()

	# returns the name of the file
	return filename + '.wav'


# Function converts speech to text
def speech_to_text(file):

	# opens the saved audio file and reads data
	with open(file, "rb") as audio_content:

		# makes api call and converts the JSON to dictionary
		response = json.loads(json.dumps(stt.recognize(audio_content, content_type="audio/wav")))
		transcript = ""

		# Makes sure that results in response is not empty
		if len(response['results']) > 0:

			# obtains the transcript as a string
			response = response['results'][0]['alternatives'][0]
			transcript = (response['transcript'])
			print(transcript)

		# returns the transcript
		return transcript


# Function saves the transcript
def save_transcript(transcript, file):

	with open(file, "w") as save:
		save.write(transcript)


# Main Function for audio input
def listen(idle):

	# Setup
	p = pyaudio.PyAudio()
	stream = p.open(format=FORMAT, channels=CHANNELS, rate=RATE, input=True, frames_per_buffer=CHUNK)

	# stores the final recording
	recording = []

	# stores all unfiltered incoming audio
	audio_in = deque(maxlen=1 * int(RATE/CHUNK))

	# initial overlap compensates for a late recording by combining 5 secs prior of audio
	initial_overlap = deque(maxlen=int(0.5 * RATE/CHUNK))
	started = False

	# single iteration
	iterations = 1

	# while loop runs until first iteration ends
	while iterations > 0:

		# reads and saves from the data stream
		current_data = stream.read(CHUNK, exception_on_overflow = False)
		audio_in.append(math.sqrt(abs(audioop.avg(current_data, 4))))

		# waits for intensity of audio to exceed idle intensity
		if sum([i > idle for i in audio_in]) > 0:

			# starts recording
			if not started:

				print("Starting Recording")
				started = True

			# otherwise, appends to the saved recording
			recording.append(current_data)

		# ends recording if intensity is not met
		elif started:

			print("Finished Recording")
			
			# iterations is 0
			iterations -= 1

			# saves audio to .wav file
			# includes the initial overlap audio to compensate for late reaction timing
			file = save_to_wav(list(initial_overlap) + recording, p)

			# closes the stream
			stream.close()
			p.terminate()

			# converts .wav to text through call to Watson API
			response = speech_to_text(file)

			# makes sure that response exists
			if response:

				# removes all spacing before and after and forces lower case
				response.lstrip().rstrip().lower()

				# saves the transcript
				save_transcript(response, os.getcwd() + "/Files/Transcript.txt")
			
			# otherwise, if transcript is not defined
			else:

				# calls text to speech
				tts.tts_and_play("Sorry, I did not get that")

			# directory of temporary audio file
			file_dir = os.path.join(os.getcwd(), file)

			# deletes the temporary audio file after a response is obtained
			if os.path.isfile(file_dir):
				os.unlink(file_dir)

			# returns the transcript
			return response

		# if intensity is not met and recording has not yet started
		else:

			# adds to the initial overlap
			initial_overlap.append(current_data)


# Only executes when this file is run
if __name__ == '__main__':

	# Simple testing code
	while True:
		response = listen(2500)
		if response:
			print(response)
			if response.startswith("goodbye"):
				print("OK. I'm going back to sleep.")
				break
