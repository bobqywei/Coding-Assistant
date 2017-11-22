import pull_code
import speech_to_text as stt
import subprocess
import os
import time
import text_to_speech as tts
from Functions import Functions

cwd = os.getcwd()

RUNNING = True

code_file_name = "temp.c"

while RUNNING:

	stt_transcript = stt.listen(2500)
	
	if stt_transcript.startswith("show"):
		url_result = pull_code.get_url_result(stt_transcript)

		if url_result:
			pull_code.get_save_code(url_result, code_file_name)
			# pull_code.run_in_terminal(code_file_name)
			tts.tts_and_play("I have also found some code for you")
		else:
			tts.tts_and_play("Sorry I could not find anything for that")

	elif stt_transcript.startswith("goodbye") or stt_transcript.startswith("good bye"):
		tts.tts_and_play("OK. I'm going back to sleep.")
		RUNNING = False

	else:
                with open(os.getcwd() + "/Files/Speech.txt", "w") as f:
                        f.write(stt_transcript)
                        
                if stt_transcript:
                        bash = subprocess.Popen(["java", "Main"], stdout=subprocess.PIPE)
                        bash.communicate()
                        tts.tts_and_play(tts.read_from_file(cwd + "/Files/Status.txt"))
                        
		
