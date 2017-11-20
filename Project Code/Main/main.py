import pull_code
import speech_to_text
import subprocess

stt_transcript = speech_to_text.listen(2500)
code_file_name = "temp.c"

with open("/home/pi/Desktop/se101-f17-group-27/Project Code/Main/Files/Speech.txt", "w") as f:
    f.write(stt_transcript)

if input.startswith("show"):
	url_result = pull_code.get_url_result(stt_transcript)

	if url_result:
		pull_code.get_save_code(url_result, code_file_name)
	else:
		print("I'm sorry, I could not find any relevant code")

	pull_code.run_in_terminal(code_file_name)

else:
	bash = subprocess.Popen(["javac", "Main.java"])
	bash2 = subprocess.Popen(["java", "Main"])
