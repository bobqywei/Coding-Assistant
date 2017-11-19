import pull_code
import speech_to_text

input = speech_to_text.listen(2500)
file_name = "temp.c"

if ("google") in input:
    url_result = pull_code.get_url_result(input)
    pull_code.get_save_code(url_result, file_name)
    pull_code.run_in_terminal(file_name)
else:
    print("test")

