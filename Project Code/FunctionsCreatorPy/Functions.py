class Functions: 
#GOAL: not operator on pre boolean expression, for,while, data type, print and and/or.. also import calculator's constants and function to evaluate single word numbers or stuff like that
	OPERATORS = ["plus", "minus", "times", "divide", "divided", "equal", "equals", "not", "lessthan", "greaterthan"] # need support for "divided by", only divide works for now
	SYMBOLS = ["+", "-", "*", "/", "/", "=", "=", "!"]
	KEYWORDS = ["and", "int", "double", "char", "if", "then", "else if", "elseif", "else"] # i.e. any word that for certain DEFINES what the line is supposed to do
	INDENTKEYS = ["if"]
	
	fileObj = None
	string = ""
	stringArray = []
	tempArray = None
	iterator = 0
	#next steps: when use is done, auto add curly braces at end, i.e. when user sayds "exit auto mode", run nextblock until counter = 0, then print out to file again (as always)
	indentCounter = 0
	indents = "" #adds \t everytime we see something in INDENTKEY.. removes \t everytime we hear "end brace" (i.e. end of if, end of for loop, end of while loop... in all cases we want to go to the next highest local indentation i.e. one less)
	# do and logic to fix nested if problem i.e. if we are in a boolean expression (limit to if for now, can make a func later to let it be used generally) then it is used to join two boolean expressions... else, it is just being used to start a new line. fix nested if "then" problem.
	#one potential fix is to force the user to say "end" when they are finished saying their sentence completely. then i wouldn't need to account for major problems like 'then' hanging

	def __init__(self, fileName=None):
		if fileName != None:
			self.fileObj = open(fileName, "w")

	def changeFile(self, fileName):
		self.fileObj = open(fileName, "w")

	def newInput(self, string):
		self.string = string
		self.tempArray = self.string.split(" ")
		self.coordinator() # the moment you detect new input, you want to put it to the text file. so run coordinator to normalize/translate/determine which function to use to check if the input is valid go on from there

#standard is add ; to current line, and also add newlines and tab to the end of each line for the next line.
	def coordinator(self):
		if self.string == "end brace " or self.string == "end loop " or self.string == "end if " :
			if self.stringArray[-1][-1] == '\t': #i.e. if we already have tab values, then we also have semicolons. else, no tab values probably implies no semicolons (by the standard).. we may have the else case for stuff like if -> then-> and ->end brace->if
				self.stringArray[-1] = self.stringArray[-1][:-1] #remove one tab to properly place brace
				self.stringArray[-1] += "}\n" + self.indents[:-1]
			else:
				self.stringArray[-1] += ";\n" + self.indents[:-1] + "}\n" + self.indents[:-1]
			self.indentCounter-=1
			self.indents = self.indents[:-2]
		else:
			self.translate()
			self.normalize()
			self.stringArray.extend(self.tempArray)
			 # iterator is the index of word in stringArray, j is the index of the found keyword in the KEYWORD array, and k is the index of the next found keyword in stringArray
			while self.iterator < len(self.stringArray): # go through every single word. if i see a keyword, then we decide what to do then.
				if self.stringArray[self.iterator] in self.KEYWORDS:
					j = self.KEYWORDS.index(self.stringArray[self.iterator])
					k = len(self.stringArray)
					if not set(self.KEYWORDS).isdisjoint(self.stringArray[self.iterator+1:]):
						for l in range(self.iterator+1, len(self.stringArray)):
							if self.stringArray[l] in self.KEYWORDS:
								k = self.KEYWORDS.index(self.stringArray[l])
								break
					if self.stringArray[self.iterator] in self.INDENTKEYS:
						self.indentCounter+=1 
						self.indents+='\t'
					if j < 1: #and implies end of statement
						self.stringArray[self.iterator-1] += ';\n' + self.indents
						del(self.stringArray[self.iterator])
						self.iterator-=1
					elif j < 4: # matches to data type sections
						self.stringArray[k-1] += ";\n" + self.indents
					elif j < 9: # then we found a matching keyword to the IF's section. note that if we see else if or else, we assume that it is for the most recent one.
						if self.stringArray[self.iterator] == "if": # try combining common stuff
							self.stringArray[self.iterator] += " ("
						elif self.stringArray[self.iterator] == "elseif": # so end of last statement
							self.stringArray[self.iterator-1] += ";\n" + self.indents[:-1]
							self.stringArray[self.iterator] = "} else if (" 
						elif self.stringArray[self.iterator] == "else": # same
							self.stringArray[self.iterator-1] += ";\n" + self.indents[:-1]
							self.stringArray[self.iterator] = "} else {\n" + self.indents 
							self.stringArray[k-1] += ";\n" + self.indents[:-1] + "}"
						elif self.stringArray[self.iterator] == "then":
							self.stringArray[self.iterator] = ") {\n" + self.indents
				self.iterator+=1
		print("MY NEW THING:")
		print(self.arrayToString(self.stringArray))
		self.fileObj.write(self.arrayToString(self.stringArray))

	def translate(self): # translate to C code format DIRECTLY CHANGES WORDS TO SYMBOLS
		for i in range(len(self.OPERATORS)):
			while self.OPERATORS[i] in self.tempArray:
				self.tempArray[self.tempArray.index(self.OPERATORS[i])] = self.SYMBOLS[i]

	def normalize(self): # filters uneccessary words in string, and changes some words (e.g. divided to divide) and adds a new line character (in addition to new line characters within the code, which is modified by specific functions). these new line characters are automatically interpretted by .write.. CONSIDER: what if i WANT a new line character to actually be in my code, not be consumed here?
		#print(self.tempArray)

		#turning all "integers" and "characters" (and possibly pointers, and arrays) to int and char so it doesn't get filtered out:
		while "integer" in self.tempArray:
			self.tempArray[self.tempArray.index("integer")] = "int"
		while "character" in self.tempArray:
			self.tempArray[self.tempArray.index("character")] = "char"
		# common words pre-naming var are "called" and "named". we search for these words, if they don't show up default is just around =. also , search after "equals" for any "to" as in let this equal to, since "to" is fairly common after equals
		if "called" in self.tempArray:
			self.tempArray[self.tempArray.index("=")-1] = self.tempArray[self.tempArray.index("called")+1]
		if "named" in self.tempArray:
			self.tempArray[self.tempArray.index("=")-1] = self.tempArray[self.tempArray.index("named")+1]
		if "to" in self.tempArray:
			self.tempArray[self.tempArray.index("=")+1] = self.tempArray[self.tempArray.index("to")+1]

		#turning all potential else ifs to elseif for ease of use in array: (and also lessthan and greaterthan)
		self.keywordConcatenator("elseif", "else", "if")
		self.keywordConcatenator("lessthan", "less", "than")
		self.keywordConcatenator("greaterthan", "else", "if")

		#print(self.tempArray)

		self.tempArray.append(None) #to ensure normalizeOps doesn't crash. note this will automatically get filtered out eventually
		# stores every NECCESSARY word into a different array, then let self.tempArray = that array in the end (the "normalizing/standardizing" part)
		tempArray = []
		ind = 0
		while (ind < len(self.tempArray)): 
			if self.tempArray[ind] == self.KEYWORDS[0]: # i.e. keyword is and. three types of ands: one for new line, one for the actual operator, and one that is useless. ASIDE: for some reason python keeps thinking there is a '\n' in self.LOGIC
				i = len(tempArray)-1
				while (i >= 0):
					if tempArray[i] in self.SYMBOLS and tempArray[i-1] in self.SYMBOLS: 
						for j in range(ind, len(self.tempArray)):
							if self.tempArray[j] in self.SYMBOLS: # RHS we see a symbol first and adjacent to it is another symbol (b/c if only one symbol, probably just a+b and they want that on a new line), so we are likely joining two booleans
								if self.tempArray[j+1] in self.SYMBOLS:
									tempArray.append("&&") #i.e. we see a boolean comparison the earliest, and what we are joining (i.e. what is the closest thing that we are going to say right side of the and statement) is also a boolean comparison (and not a keyword), so they must mean to use &&
									break
								else: # we are assuming that OR will not be used for anything besides boolean expressions (if we even do or..)
									tempArray.append("and") # we will deal with this later to insert new lines
									break
							elif self.tempArray[j] in self.KEYWORDS:
								tempArray.append("and")
								break
						break
					elif tempArray[i] in self.SYMBOLS or tempArray[i] in self.KEYWORDS[3:]: #we usually don't mean anything when 'and' is next to a data type
						#i.e. the second most likely case is that the user is already feeding input so they want a new line character
						tempArray.append("and")
						break
					i-=1 #no else case required because we do whant ot search the entire thing. if nothing gets done, that must mean the and was useless
			elif (self.tempArray[ind] in self.KEYWORDS): #guarenteed cases or where we for certain want to store just the current index into the new array
				if (self.tempArray[ind] == "if" and "if" in self.tempArray[ind+1:] and "then" not in self.tempArray[ind:ind+self.tempArray[ind+1:].index("if")+1]): #we don't want to count if's twice UNLESS there is a 'then' in between the two... note that we MUST ignore the correct one (i.e. the first one if we have 'create an if statement and basically if....') because if we don't, and may possibly not work correctly
					ind+=1
					continue
				else:
					tempArray.append(self.tempArray[ind])
			elif (self.tempArray[ind] in self.SYMBOLS):
				tempArray.append(self.tempArray[ind-1]) #we are assuming that we USUALLY get an operator that is binary. so store the thing before the operator
				end = self.normalizeOps(ind)
				for j in range(ind, end+1): #i.e. between the current operator and thte var adjacent to the last relevant local operator
					tempArray.append(self.tempArray[j]) #store everything in between
				ind = end #after i++, start searching at indice after the last relevant thing
			ind+=1 #start seraching for the next term
		self.tempArray = tempArray
		tempArray = None
		#print(self.tempArray)

	def keywordConcatenator(self, result, keyword1, keyword2):
		if keyword1 in self.tempArray: 
			for i in range(self.tempArray.index(keyword1), len(self.tempArray)):
				if (i+1 < len(self.tempArray) and self.tempArray[i+1] == keyword2): # i.e. we have else in one index, and if in the other index. user probably wnats else if
					self.tempArray[i] = result
					del self.tempArray[i+1]
					if keyword1 in self.tempArray[i+1:]:
						i = self.tempArray[i+1:].index(keyword1) - 1
					else: 
						break

	def normalizeOps(self, index): #returns an index, i.e. end of relevant stuff. we require the starting index (i.e. where we find an operator), and then search forwards for more operators. note we are gaurenteed to not have any opersators behind because we search in order
		if (self.tempArray[index+1] not in self.SYMBOLS and self.tempArray[index+2] not in self.SYMBOLS): #if next two are not SYMBOLS, since we may have + 3 +, in which case we want to continue adding indices. note we are guarenteed that the second to last term will never be an operator, and the very last term is always \n.. HOWEVER, we may have the case where the user pauses and an operator is hanging.. ACCOUNT FOR THIS LATER
			return index+1 #i.e. grab everything adjacent (will be vars) to this, since this is the last local operator.
		else:
			return self.normalizeOps(index+1)

	def arrayToString(self, array):
		string = ""
		for i in range(len(array)): 
			if i is len(array) - 1 or (i+1 is not len(array) and array[i] in self.SYMBOLS and array[i+1] in self.SYMBOLS) or "(" in array[i] or (i+1 is not len(array) and ")" in array[i+1]) or "\n" in array[i]: # very first condition: if this is the last thing, we likely DO NOT want an extrra space. reasons: user is finished line, so extra space doesnt matter. but if user does not finish line, some complications: end with "plus", start with "equals"... once again another conflict is "equals" and "not" -> I THINK SEPARATING THIS IS EASY. JUST IGNORE CONCATENATION WHEN YOU SEE =!, AS ! BELONGS TO THE NEXT... BUT DON'T IGNROE != BECAUSE IT ALWAYS BELONGS TO =.. if this is not the last iterations, and if current and next element are operators, want to likely concatenate 
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
	#funcs.newInput("create an integer called a and let it equal to three") #use calculator class (make one giant list of every list, i.e. one array of ALL, and see if any of these wrds in that list -> clac class)
	funcs.newInput("create an if statement and basically if a equals equals b then a plus b and a plus c") #consider the separate case. then [i-1] wont add a semi colon
	#funcs.newInput("end brace ")
	funcs.newInput("also have that else if a not equals b then a minus b")
	#funcs.newInput("create an if statement and basically in this if a equals equals three then a plus b and if c equals equals b then a plus b and a plus c else if a minus b then a plus c else a plus b")
	#funcs.newInput("if a equals equals three then a plus b ")
	#funcs.newInput("if c equals equals ten then a minus b ") # need user to specify 'and' for in block or 'nextblock' for out of block.
	funcs.newInput("end if ")

	funcs.newInput("uhhh give me another if statement where if a equals b then a plus c and also if c equals d then")
	funcs.newInput("give me a new integer called f and let it be equal to ten")
	funcs.newInput("end if ")
	funcs.newInput("end if ")
	#funcs.newInput("create an integer called c and let it equal to four")
	funcs.closeFile() # close stream to free up memory (only do after you are done everything)