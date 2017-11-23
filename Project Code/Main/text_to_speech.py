"""
	File contains functions for converting from text to speech
"""

# imports the following modules
import os
import pyaudio
import wave
from watson_developer_cloud import TextToSpeechV1

# IBM credentials for text to speech
IBM_USERNAME_TTS = "7b4821e4-082d-49b9-aaac-00be41619530"
IBM_PASSWORD_TTS = "M7FimeKQxQSg"

# instance of IBM text to speech
tts = TextToSpeechV1(username=IBM_USERNAME_TTS, password=IBM_PASSWORD_TTS)


# function for reading all text from file
def read_from_file(file_name):

	file_content = ""
	
	# opens the file and gets the string from each line
	with open(file_name, 'r') as f:
		lines = f.readlines()
		for line in lines:
			if line.strip():
				file_content += line.strip()
				file_content += " "

	# returns the stored string
	return file_content


# function plays audio from a file
def play_audio(file_name):

	# sets up file and PyAudio
	file = wave.open(file_name, "rb")
	py_audio = pyaudio.PyAudio()

	# opens the audio stream
	stream = py_audio.open(format=pyaudio.get_format_from_width(file.getsampwidth()),
						   channels=file.getnchannels(),
						   rate=file.getframerate(),
						   output=True
						   )

	# read in bytes from file
	data = file.readframes(1024)

	# plays the audio stream
	while data:
		stream.write(data)
		data = file.readframes(1024)

	# closes the stream and terminates
	stream.stop_stream()
	stream.close()
	py_audio.terminate()


# function converts text to speech and plays the audio
def tts_and_play(text):

	# temporary file 
	temp_file_name = "response.wav"

	# converts the text to speech and stores the audio
	with open(temp_file_name, 'wb') as speech_response_file:
		speech_response_file.write(tts.synthesize(text, accept='audio/wav', voice="en-US_AllisonVoice"))

	# plays the audio with call to function
	play_audio(temp_file_name)

	# deletes temporary file
	temp_file_dir = os.path.join(os.getcwd(), temp_file_name)
	if os.path.isfile(temp_file_dir):
		os.unlink(temp_file_dir)


# test code
if __name__ == '__main__':
	filename = "test.txt"
	text_tts = read_from_file(filename)
	tts_and_play(text_tts)
