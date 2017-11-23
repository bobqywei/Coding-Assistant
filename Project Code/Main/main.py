"""
	Main project file manages other functions and classes
"""

# imports necessary files
import pull_code
import speech_to_text as stt
import text_to_speech as tts
from Functions import Functions

# imports necessary modules
import subprocess
import os
import time


# calls new instance of functions class
funcs = Functions()

# stores the current working directory
cwd = os.getcwd()

# current state
RUNNING = True

# name of file
code_file_name = "temp.c"

# Main loop
while RUNNING:

	# calls listen and obtains transcript
	stt_transcript = stt.listen(2500)
	
	# calls the specific functions based on user input
	if stt_transcript.startswith("show"):

		# obtains the url of the best matching website
		url_result = pull_code.get_url_result(stt_transcript)

		# makes sure that the url exists
		if url_result:

			# pulls and saves the code from the website
			pull_code.get_save_code(url_result, code_file_name)
			
			# plays the message
			tts.tts_and_play("I have also found some code for you")
		
		# if url does not exist, message is played
		else:
			tts.tts_and_play("Sorry I could not find anything for that")

	# terminates the program if given the command
	elif stt_transcript.startswith("goodbye") or stt_transcript.startswith("good bye"):

		tts.tts_and_play("OK. I'm going back to sleep.")

		RUNNING = False

	# calls associated java functions for handling file management
	elif stt_transcript.startswith("raspberry "):

		with open(os.getcwd() + "/Files/Speech.txt", "w") as f:
			f.write(stt_transcript)
            
		bash = subprocess.Popen(["java", "Main"], stdout=subprocess.PIPE)
		bash.communicate()

		tts.tts_and_play(tts.read_from_file(cwd + "/Files/Status.txt"))

	# calls python class for handling file input  
	else:
		with open(os.getcwd() + "/Files/Speech.txt", "w") as f:
			f.write(stt_transcript)
		
		funcs.newInput()
                        
		
