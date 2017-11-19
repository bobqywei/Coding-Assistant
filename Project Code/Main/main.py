import pull_code
import speech_to_text
import subprocess

input = speech_to_text.listen(2500)
file_name = "temp.c"

with open("/home/pi/Desktop/se101-f17-group-27/Project Code/Main/Files/Speech.txt", "w") as f:
    f.write(input)

if input.startswith("show"):
    url_result = pull_code.get_url_result(input)
    pull_code.get_save_code(url_result, file_name)
    pull_code.run_in_terminal(file_name)
    
else:
    bash = subprocess.Popen(["javac", "Main.java"])
    bash2 = subprocess.Popen(["java", "Main"])
