class Functions: 
#GOAL: "greater than or equal to", *****not operator on pre boolean expression (if i see not and there isn't an equals after it, then we look for thte boolean expression follwing not), **for,**while, data type, **print and and/or.. also **import calculator's constants and function to evaluate single word numbers or stuff like that
	OPERATORS = ["plus", "minus", "times", "divide", "divided", "mod", "modulo", "equal", "equals", "not", "lessthan", "greaterthan"] # need support for "divided by", only divide works for now. **"is equal to" should be translated to equals equals (right now, is, to gets deleted and only equals remain, which works for vars i.e. int a is equal to b. bu tthis compromoises stuff like if a is equal to b). **add break
	SYMBOLS = ["+", "-", "*", "/", "/", "%", "%", "=", "=", "!", "<", ">"]
	BOOLSYM = ["=", "!", "<", ">"]
	KEYWORDS = ["and", "or", "int", "double", "char", "if", "then", "else if", "elseif", "else", "print", "endprint"]
	LOGICKEYWORDS = ["and", "or"] # i.e. any word that for certain DEFINES what the line is supposed to do
	DATAKEYWORDS = ["int", "double", "char"]
	FORMATTERS = ["%d", "%f", "%c"]
	IFKEYWORDS = ["if", "then", "else if", "elseif", "else"]
	PRINTKEYWORDS = ["print", "endprint"]
	INDENTKEYS = ["if"]
	NAMEWHITELIST = [] # this is a whitelist that builds... it contains only variables so we can use it later for stuff like printf() statements to match with the formatting signs
	DATAWHITELIST = []
	INDEPDATALIST = [] #i.e. list of declared variables that were not inited
	#IMPORTANT WORDS: 'and' usually for new line. 'end brace/if/loop' usually for ending the current block and jumping to one level higher. 'end print' to stop reading input for 'print'
	#REMINDER: EVERYTIME I ADD A KEYWORD, CONSIDER ADDING IT TO AND'S AND OR'S LIST OF "ISDISJUNCTION" AND LIST OF "IF SELF.CURRENTARRAY[J] IN ___KEYWORD"
	#next steps: line 173, instead of indexing for endprint, what if print and endprint are on two diff lines? then we want to kepe reading input line after line until endprint shows up
	#ALSO: if we have no blocks at all, want end brace/loop/if to close with semi colon only, no brace. but this shouldnt happen due to class creation..
	fileObj = None
	string = ""
	stringArray = []
	currentArray = None
	iterator = 0
	#next steps: when use is done, auto add curly braces at end, i.e. when user sayds "exit auto mode", run nextblock until counter = 0, then print out to file again (as always)
	indentCounter = 0
	indents = "" #adds \t everytime we see something in INDENTKEY.. removes \t everytime we hear "end brace" (i.e. end of if, end of for loop, end of while loop... in all cases we want to go to the next highest local indentation i.e. one less)
	# do and logic to fix nested if problem i.e. if we are in a boolean expression (limit to if for now, can make a func later to let it be used generally) then it is used to join two boolean expressions... else, it is just being used to start a new line. fix nested if "then" problem.
	#one potential fix is to force the user to say "end" when they are finished saying their sentence completely. then i wouldn't need to account for major problems like 'then' hanging
	#problems atm: int; f=ten; i.e. and ambiguity can't filter and (if-and-if) and multi line var declaration/ multiple "called"'s in one line
	def __init__(self, fileName=None):
		if fileName != None:
			self.fileObj = open(fileName, "w")

	def changeFile(self, fileName):
		self.fileObj = open(fileName, "w")

	def newInput(self, string):
		self.string = string
		self.currentArray = self.string.split(" ")
		self.coordinator() # the moment you detect new input, you want to put it to the text file. so run coordinator to normalize/translate/determine which function to use to check if the input is valid go on from there

#standard is add ; to current line, and also add newlines and tab to the end of each line for the next line.
	def coordinator(self):
		if self.string == "end brace" or self.string == "end loop" or self.string == "end if" :
			if self.stringArray[-1][-1] == '\t': #i.e. if we already have tab values, then we also have semicolons. else, no tab values probably implies no semicolons (by the standard).. we may have the else case for stuff like if -> then-> and ->end brace->if
				self.stringArray[-1] = self.stringArray[-1][:-1] #remove one tab to properly place brace
				self.stringArray[-1] += "}\n" + self.indents[:-1]
			else:
				self.stringArray[-1] += ";\n" + self.indents[:-1] + "}\n" + self.indents[:-1]
			self.indentCounter-=1
			self.indents = self.indents[:-1]
		else:
			self.translate()
			self.normalize()
			self.stringArray.extend(self.currentArray)
			 # iterator is the index of word in stringArray, j is the index of the found keyword in the KEYWORD array, and k is the index of the next found keyword in stringArray
			while self.iterator < len(self.stringArray): # go through every single word. if i see a keyword, then we decide what to do then.
				k = len(self.stringArray)
				if not set(self.KEYWORDS).isdisjoint(self.stringArray[self.iterator+1:]):
					for l in range(self.iterator+1, len(self.stringArray)):
						if self.stringArray[l] in self.KEYWORDS:
							k = l
							break
				if self.stringArray[self.iterator] in self.INDENTKEYS:
					self.indentCounter+=1 
					self.indents+='\t'
				if self.stringArray[self.iterator] in self.LOGICKEYWORDS: #and implies end of statement MIGHT ASWELL JUST SWITHC TO ==AND
					self.stringArray[self.iterator-1] += ';\n' + self.indents
					del(self.stringArray[self.iterator])
					self.iterator-=1
				elif self.stringArray[self.iterator] in self.DATAKEYWORDS and self.stringArray[self.iterator+1] in self.INDEPDATALIST: # matches to data type sections
					self.stringArray[self.iterator-1] += ";\n" + self.indents #WE DON'T NEED TO DO ANYTHIGN TO DATAKEYWORDS BECAUSE THEY ONLY NEED FORMATTING FROM 'AND' and co.
				elif self.stringArray[self.iterator] in self.IFKEYWORDS: # then we found a matching keyword to the IF's section. note that if we see else if or else, we assume that it is for the most recent one.
					if self.stringArray[self.iterator] == "if": # try combining common stuff
						self.stringArray[self.iterator] += " ("
					elif self.stringArray[self.iterator] == "elseif": # so end of last statement
						self.stringArray[self.iterator-1] += ";\n" + self.indents[:-1]
						self.stringArray[self.iterator] = "} else if (" 
					elif self.stringArray[self.iterator] == "else": # same
						self.stringArray[self.iterator-1] += ";\n" + self.indents[:-1]
						self.stringArray[self.iterator] = "} else {\n" + self.indents 
						self.stringArray[k-1] += ";\n" + self.indents
					elif self.stringArray[self.iterator] == "then":
						self.stringArray[self.iterator] = ") {\n" + self.indents
				elif self.stringArray[self.iterator] in self.PRINTKEYWORDS:
					if self.stringArray[self.iterator] == "print": # check if any vars. if yes, replace the var in the stringArray with the %sign, then add comma to end and do corressponding order of w/e
						self.stringArray[self.iterator] = 'printf("'
				self.iterator+=1
		print("MY NEW THING:")
		print(self.arrayToString(self.stringArray))
		self.fileObj.write(self.arrayToString(self.stringArray))

	def translate(self): # translate to C code format DIRECTLY CHANGES WORDS TO SYMBOLS
		#turning all potential else ifs to elseif for ease of use in array: (and also lessthan and greaterthan)
		self.keywordConcatenator("elseif", "else", "if")
		self.keywordConcatenator("lessthan", "less", "than")
		self.keywordConcatenator("greaterthan", "greater", "than")
		self.keywordConcatenator("endprint", "end", "print")
		for i in range(len(self.OPERATORS)):
			while self.OPERATORS[i] in self.currentArray:
				self.currentArray[self.currentArray.index(self.OPERATORS[i])] = self.SYMBOLS[i]

	def normalize(self): # filters uneccessary words in string, and changes some words (e.g. divided to divide) and adds a new line character (in addition to new line characters within the code, which is modified by specific functions). these new line characters are automatically interpretted by .write.. CONSIDER: what if i WANT a new line character to actually be in my code, not be consumed here?
		#print(self.currentArray)

		#turning all "integers" and "characters" (and possibly pointers, and arrays) to int and char so it doesn't get filtered out:
		while "integer" in self.currentArray:
			self.currentArray[self.currentArray.index("integer")] = "int"
		while "character" in self.currentArray:
			self.currentArray[self.currentArray.index("character")] = "char"
		# common words pre-naming var are "called" and "named".... they fit between dataType and varName most of the time so we can just remove it. we search for these words, if they don't show up default is just around =. also , search after any operator for any "to" as in let this equal to, or if a equal euqal to b since "to" is fairly common after equals
		self.commonWordDel("called")
		self.commonWordDel("named")
		self.commonWordDel("to")
		self.commonWordDel("is")
		#print(self.currentArray)

		#note: if we ever see the word "it" or 'which', we assume it only follows from after the declaration of a variable because there is no other case where "it" isn't ambiguous. so if we see it, we should also see an "equals" soon. 
		#what we have: type - var name -....- it-..-equals.. but what if type-varname-equals? then we just check if immediately next to var name is equals. if not, we check if there is an "it" in the rest of the string. if not, then assume no init
		#note we asusme that they are initializing and declaring on one line if they want to. i.e. if they have and let it equals to on a different line, we don't accept it
		#filtering out key stuff from var declaration/initialization
		print(self.currentArray)
		if "or" in self.currentArray or not set(self.DATAKEYWORDS).isdisjoint(self.currentArray): #note that this iterates through the entire array so we can just check all outlier cases here (i.e. including the "> or ="/"< or =" case)
			j=0
			while j < len(self.currentArray):
				if self.currentArray[j] == "or" and self.currentArray[j+1] == "=" and (self.currentArray[j-1] == "<" or self.currentArray[j-1] == ">"):
					del self.currentArray[j]
					j-=1
				elif self.currentArray[j] in self.DATAKEYWORDS:
					# if equals is right beeside the variable already then just coninute searching (can't leav eit out otherwise we invoke else)
					if "=" in self.currentArray[j:] and self.currentArray[j+2] == "=":
						j+=1
						continue
					elif "it" in self.currentArray[j:] or "which" in self.currentArray[j:]: # equals is somewhere in the distance, but is the first equals to show up and "it" implies that the variable is getting inited
						self.DATAWHITELIST.append(self.currentArray[j])
						self.NAMEWHITELIST.append(self.currentArray[j+1])
						inc = self.currentArray[j:].index("=")-1
						self.currentArray = self.currentArray[:j+2] + self.currentArray[j+self.currentArray[j:].index("="):] #we require the [j:] because what if multi var declaration
						j+=inc
					else: # there is no equals anywhere that belongs to the datatype... so we did not init
						self.INDEPDATALIST.append(self.currentArray[j+1])
				j+=1

		self.currentArray.append(None) #to ensure normalizeOps doesn't crash. note this will automatically get filtered out eventually
		self.currentArray.append(None)
		# stores every NECCESSARY word into a different array, then let self.currentArray = that array in the end (the "normalizing/standardizing" part)
		tempArray = []
		ind = 0
		print(self.currentArray)
		while (ind < len(self.currentArray)): 
			# if we see 'and', then we check if the stringArray or tempArray actually contians anything yet for us to join. if not, do nothing (i.e. don't append and). if there is, then we check if the next thing that shows up is a) a boolean expression or b) a keyword/normal operator. if the first ting that hsows up is aboolean expression, then we use && and don't need to check if the backwards case is also boolean because it is assumed (there is not other reason for the user to use and (boolean) if there isn't a boolean already preceding the and)
			if self.currentArray[ind] == "and": # i.e. keyword is and. three types of ands: one for new line, one for the actual operator, and one that is useless. looking 
				#CONSIDER: WHY NOT JUST MAKE AN ARRAY OF ACCEPTED VALUES FOR AND AND PUT IT HERE INSTEAD?
				#NOTE: THIS IS CURRENTLY FLAWED I.E. DOESN'T WORK FOR PRINT B/C WHEN IT GETS AROUND TO CHECKING THIS (if we have and on a new line that is, and on the same line is fine), PRINT HAS BECOME PRINT(", WE CAN FIX EITHER BY CONCATENATING (" TO THE NEXT WORD OR BY CHANGING THE CONDITION TO PRINT("
				#ANOTHER POSSIBLE FIX IS TO JUST EXCLUDE IT ENTIRELY, BUT IS THIS EFFICIENT?
				#if not ((set(self.stringArray).isdisjoint(self.SYMBOLS) and set(self.stringArray).isdisjoint(self.IFKEYWORDS) and set(self.stringArray).isdisjoint(self.PRINTKEYWORDS)) and (set(tempArray).isdisjoint(self.SYMBOLS) and set(tempArray).isdisjoint(self.IFKEYWORDS) and set(tempArray).isdisjoint(self.PRINTKEYWORDS))): #note that we only need to check for keywords that are not data types because we don't want and's to have anything to do with them
					for j in range(ind, len(self.currentArray)):
						if self.currentArray[j] in self.BOOLSYM and (self.currentArray[j+1] in self.BOOLSYM or self.currentArray[j] == "<" or self.currentArray[j] == ">"):
							tempArray.append("&&")
							break
						elif self.currentArray[j] in self.IFKEYWORDS or self.currentArray[j] in self.PRINTKEYWORDS or self.currentArray[j] in self.SYMBOLS:
							tempArray.append("and")
							break
			# we will assume that or is only used for boolean expressions
			elif self.currentArray[ind] == "or":
				#similar to and, if we see a boolean expression follow then we automatically know that preceding isi also boolean. if we find keyword or other symbol first, then do nothing
				#if not ((set(self.stringArray).isdisjoint(self.SYMBOLS) and set(self.stringArray).isdisjoint(self.IFKEYWORDS) and set(self.stringArray).isdisjoint(self.PRINTKEYWORDS)) and (set(tempArray).isdisjoint(self.SYMBOLS) and set(tempArray).isdisjoint(self.IFKEYWORDS) and set(tempArray).isdisjoint(self.PRINTKEYWORDS))): #note that we only need to check for keywords that are not data types because we don't want and's to have anything to do with them
					for j in range(ind, len(self.currentArray)):
						if self.currentArray[j] in self.BOOLSYM and (self.currentArray[j+1] in self.BOOLSYM or self.currentArray[j] == "<" or self.currentArray[j] == ">"):
							tempArray.append("||")
							break
						elif self.currentArray[j] in self.IFKEYWORDS or self.currentArray[j] in self.PRINTKEYWORDS or self.currentArray[j] in self.SYMBOLS:
							break
			elif self.currentArray[ind] == "print":
				tempInd = ind+self.currentArray[ind:].index("endprint")
				self.currentArray[tempInd] = '"'
				for i in range(ind, tempInd+1):
					# we are assuming that if they try printing anything and that 'thing' shares a name with a prev declared var, then they want the var
					if self.currentArray[i] in self.NAMEWHITELIST:
						self.currentArray[tempInd] += ","
						self.currentArray[tempInd] += self.NAMEWHITELIST[self.NAMEWHITELIST.index(self.currentArray[i])]
						self.currentArray[i] = self.FORMATTERS[self.DATAKEYWORDS.index(self.DATAWHITELIST[self.NAMEWHITELIST.index(self.currentArray[i])])] #replacing the word with the corresonding %thing
					if i == tempInd:
						self.currentArray[i] += ")"
					tempArray.append(self.currentArray[i])
				ind = tempInd
			elif (self.currentArray[ind] in self.KEYWORDS or self.currentArray[ind] in self.INDEPDATALIST): #cases of remaining keywords or var names that aren't specifically handled. we for certain want to store just the current index into the new array
				# DO I NEED THE "THEN NOT IN SELF.CURRENTARRAY[I]"?   #if (self.currentArray[ind] == "if" and ("then" not in self.currentArray[ind:] or "if" in self.currentArray[ind+1:] and "then" not in self.currentArray[ind:ind+self.currentArray[ind+1:].index("if")+1])): #we don't want to count if's twice UNLESS there is a 'then' in between the two... note that we MUST ignore the correct one (i.e. the first one if we have 'create an if statement and basically if....') because if we don't, and may possibly not work correctly
				if (self.currentArray[ind] == "if" and "if" in self.currentArray[ind+1:] and "then" not in self.currentArray[ind:ind+self.currentArray[ind+1:].index("if")+1]): #we don't want to count if's twice UNLESS there is a 'then' in between the two... note that we MUST ignore the correct one (i.e. the first one if we have 'create an if statement and basically if....') because if we don't, and may possibly not work correctly
					ind+=1
					continue
				else:
					tempArray.append(self.currentArray[ind])
			elif (self.currentArray[ind] in self.SYMBOLS):
				tempArray.append(self.currentArray[ind-1]) #we are assuming that we USUALLY get an operator that is binary. so store the thing before the operator
				end = self.normalizeOps(ind)
				for j in range(ind, end+1): #i.e. between the current operator and thte var adjacent to the last relevant local operator
					tempArray.append(self.currentArray[j]) #store everything in between
				ind = end #after i++, start searching at indice after the last relevant thing
			ind+=1 #start seraching for the next term
		self.currentArray = tempArray
		tempArray = None
		print(self.currentArray)

	def commonWordDel(self, word):
		while word in self.currentArray:
			del self.currentArray[self.currentArray.index(word)]

	def keywordConcatenator(self, result, keyword1, keyword2):
		if keyword1 in self.currentArray: 
			for i in range(self.currentArray.index(keyword1), len(self.currentArray)):
				if (i+1 < len(self.currentArray) and self.currentArray[i+1] == keyword2): # i.e. we have else in one index, and if in the other index. user probably wnats else if
					self.currentArray[i] = result
					del self.currentArray[i+1]
					if keyword1 in self.currentArray[i+1:]:
						i = self.currentArray[i+1:].index(keyword1) - 1
					else: 
						break

	def normalizeOps(self, index): #returns an index, i.e. end of relevant stuff. we require the starting index (i.e. where we find an operator), and then search forwards for more operators. note we are gaurenteed to not have any opersators behind because we search in order
		if (self.currentArray[index+1] not in self.SYMBOLS and self.currentArray[index+2] not in self.SYMBOLS) or self.currentArray[index+2] is None: #if next two are not SYMBOLS, since we may have + 3 +, in which case we want to continue adding indices. note we are guarenteed that the second to last term will never be an operator, and the very last term is always \n.. HOWEVER, we may have the case where the user pauses and an operator is hanging.. ACCOUNT FOR THIS LATER
			return index+1 #i.e. grab everything adjacent (will be vars) to this, since this is the last local operator.
		else:
			return self.normalizeOps(index+1)

	def arrayToString(self, array):
		string = ""
		for i in range(len(array)): 
			if i is len(array) - 1 or (array[i] in self.SYMBOLS and array[i+1][0] in self.SYMBOLS) or "(" in array[i] or (i+1 is not len(array) and ")" in array[i+1]) or "\n" in array[i]: # very first condition: if this is the last thing, we likely DO NOT want an extrra space. reasons: user is finished line, so extra space doesnt matter. but if user does not finish line, some complications: end with "plus", start with "equals"... once again another conflict is "equals" and "not" -> I THINK SEPARATING THIS IS EASY. JUST IGNORE CONCATENATION WHEN YOU SEE =!, AS ! BELONGS TO THE NEXT... BUT DON'T IGNROE != BECAUSE IT ALWAYS BELONGS TO =.. if this is not the last iterations, and if current and next element are operators, want to likely concatenate 
				string += array[i]
			else:
				string += array[i] + " "
		return string

	def closeFile(self):
		self.fileObj.close()

if __name__ == "__main__": #instead we have 'code mode' or something of the sort, which initializes a functions object. then opens files on request, and when does, the person will say finished code mode or osmething, which closes the files
	funcs = Functions() # new functions object w/o intialization
	funcsInit = Functions("hello.c") # new functions object with initialized file
	funcs.changeFile("hello.c") # opens a reader stream and stores the file in fileObj property
	
	funcs.newInput("give me an integer called a and let it equal to 2")
	funcs.newInput("and print hello a there end print")
	funcs.newInput("and print why am i still alive end print")
	#THIS ONE IS REALLY GLITCHY #funcs.newInput("and print hello a there end print and print why am i still alive end print")


	#funcs.newInput("give me an new integer named ABC and it equals three and also give an integer named CDF and it equals four")
	#funcs.newInput("now uhh create an if statement where uh if ABC equals equals CDF or A is greater than or equals to B then")
	#funcs.newInput("i want to create an int G equals five and G mod equals two")
	#funcs.newInput("else if ABC greater than CDF then G plus plus")
	#funcs.newInput("else G minus minus")
	#funcs.newInput("if G equals equals ten and H equals equals eleven")
	#funcs.newInput("then if G minus G equals equals ABC then ABC plus DEF")
	#funcs.newInput("end if")
	#funcs.newInput("now in this if statement i want a plus b")
	#funcs.newInput("else if G plus G equals equals Z then G plus equals G")
	#funcs.newInput("end if")
	#funcs.newInput("end if")

	#difficult to filter out "and" and "then" w/o making mistakes

	# TEST THE BELOW AGAIN ONCE I IMPLEMENT THE WHITE LIST
	#funcs.newInput("give me a new integer f equals ten")
	#funcs.newInput("give me a new integer f")
	#funcs.newInput("give me a new integer f and g equals ten")
	#funcs.newInput("give me a new integer called f")
	#funcs.newInput("let f be equal to ten") #what if user only wants to create f but not initialize?
	#funcs.newInput("give me a new integer called f and let it be equal to ten")

	#funcs.newInput("if a equals equals b then")
	#funcs.newInput("create a new integer a which is equal to three")
	#funcs.newInput("and make an integer called b equals ten")
	#funcs.newInput("else if a not equals b then")
	#funcs.newInput("if a equals equals ten then")
	#funcs.newInput("a mod equals b")
	#funcs.newInput("and c plus equals d")
	#funcs.newInput("and make an integer called c equals two")
	#funcs.newInput("and three plus four")
	#funcs.newInput("and make an integer called d and let it be equal to two")
	#funcs.newInput("and make an integer called e equals two") ###### if we stop here, we will see that this line automatically adds its own semi colon. ( if we kept the and condition \n and the coordinator for datatypes)
	#funcs.newInput("else if a equals equals b then a plus equals b and make an integer called f")
	#funcs.newInput("and three plus four")
	#funcs.newInput("end if")
	#funcs.newInput("end if")
	#print(funcs.INDEPDATALIST)	

	funcs.closeFile() # close stream to free up memory (only do after you are done everything)