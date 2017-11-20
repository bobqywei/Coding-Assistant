import os
import pyaudio
import wave

from watson_developer_cloud import TextToSpeechV1

IBM_USERNAME_TTS = "55c80c39-8cec-4969-8701-7ea93997915c"
IBM_PASSWORD_TTS = "DYnKTSs3rWO1"
tts = TextToSpeechV1(username=IBM_USERNAME_TTS, password=IBM_PASSWORD_TTS)


def read_from_file(file_name):
	file_content = ""
	with open(file_name, 'r') as f:
		lines = f.readlines()
		for line in lines:
			if line.strip():
				file_content += line.strip()
				file_content += " "

	return file_content


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
	# play the audio stream
	while data:
		stream.write(data)
		data = file.readframes(1024)

	# stop stream and terminates PyAudio
	stream.stop_stream()
	stream.close()
	py_audio.terminate()


def tts_and_save(text):
	temp_file_name = "response.wav"

	with open(temp_file_name, 'wb') as speech_response_file:
		speech_response_file.write(tts.synthesize(text, accept='audio/wav', voice="en-US_AllisonVoice"))

	play_audio(temp_file_name)

	temp_file_dir = os.path.join(os.getcwd(), temp_file_name)

	if os.path.isfile(temp_file_dir):
		os.unlink(temp_file_dir)


if __name__ == '__main__':
	filename = "test.txt"
	text_tts = read_from_file(filename)
	tts_and_save(text_tts)
