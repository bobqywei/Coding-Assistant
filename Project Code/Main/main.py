import pull_code
import speech_to_text

input = speech_to_text.listen(4000)

file_name = "temp.c"
url_result = get_url_result(input)
get_save_code(url_result, file_name)
run_in_terminal(file_name)

