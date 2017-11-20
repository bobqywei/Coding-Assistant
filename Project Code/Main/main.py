import pull_code
import speech_to_text
import subprocess
import os


RUNNING = True

code_file_name = "temp.c"

while RUNNING:

	stt_transcript = speech_to_text.listen(2500)

	with open(os.getcwd() + "/Speech.txt", "w") as f:
		f.write(stt_transcript)

	if stt_transcript.startswith("show"):
		url_result = pull_code.get_url_result(stt_transcript)

		if url_result:
			pull_code.get_save_code(url_result, code_file_name)
			pull_code.run_in_terminal(code_file_name)
		else:
			print("I'm sorry, I could not find any relevant code")

	elif stt_transcript.startswith("goodbye"):
		print("OK. I'm going back to sleep.")
		RUNNING = False

	else:
		bash = subprocess.Popen(["javac", "Main.java"])
		bash2 = subprocess.Popen(["java", "Main"])
