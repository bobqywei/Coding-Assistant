from MiniCalculator import *
import os

class Functions: 
#GOAL: "greater than or equal to",not operator on pre boolean expression (if i see not and there isn't an equals after it, then we look for thte boolean expression follwing not), **for,while, data type, print (w/ doubles) and and/or.. also import calculator's constants and function to evaluate single word numbers or stuff like that
	# **add arrys, add length
	# note for funcs, have indentCounter and indents start at 1!!!w*** what about functions? (or classes?) indentations separate. they will always have one particular form. add to some whitelist..
	#another problem: if but no then: if we check no then at all, we can assume the if is extra. when then that would mean we can't go over several lines. 
	OPERATORS = ["plus", "minus", "times", "divide", "divided", "mod", "modulo", "equal", "equals", "not", "lessthan", "greaterthan"] # need support for "divided by", only divide works for now. **"is equal to" should be translated to equals equals (right now, is, to gets deleted and only equals remain, which works for vars i.e. int a is equal to b. bu tthis compromoises stuff like if a is equal to b). add break (auto add end loop after this statement.. NOPE DONT DO THIS. WHAT IF BREAK IS IN AN IF STATEMENT AND THEY WANT TO ADD ELSE IF?)
	SYMBOLS = ["+", "-", "*", "/", "/", "%", "%", "=", "=", "!", "<", ">"]
	BOOLSYM = ["=", "!", "<", ">"]
	KEYWORDS = ["and", "or", "int", "double", "char", "if", "then", "else if", "elseif", "else", "print", "endprint", "break", "while"]
	LOGICKEYWORDS = ["and", "or"] # i.e. any word that for certain DEFINES what the line is supposed to do
	DATAKEYWORDS = ["int", "double", "char"]
	FORMATTERS = ["%d", "%f", "%c"]
	IFKEYWORDS = ["if", "then", "else if", "elseif", "else"]
	PRINTKEYWORDS = ["print", "endprint"]
	PRINTMODE = False # this allows us to read multiple lines for the same 'print' call until and 'endprint' shows up
	ENDOFPRINTFORMAT = '"'
	INDENTKEYS = ["if", "while"]
	NAMEWHITELIST = [] # this is a whitelist that builds... it contains only variables so we can use it later for stuff like printf() statements to match with the formatting signs
	DATAWHITELIST = []
	INDEPDATALIST = [] #i.e. list of declared variables that were not inited
	#IMPORTANT WORDS: 'and' usually for new line. 'end brace/if/loop' usually for ending the current block and jumping to one level higher. 'end print' to stop reading input for 'print'
	#ASSUMPTION: after we say while, we do not say anything UNLESS we follow it up with a "and"
	#optional**REMINDER: EVERYTIME I ADD A KEYWORD, CONSIDER ADDING IT TO AND'S AND OR'S LIST OF "ISDISJUNCTION" AND LIST OF "IF SELF.CURRENTARRAY[J] IN ___KEYWORD"
	#optional****IMPLY AND KEYWORDS i.e. check after the several if/elifs a separte if if it's in this array. if so, check if there is an and preceding this keyword. no? add a and
	fileName = ""
	string = ""
	stringArray = []
	currentArray = None
	iterator = 0

	indentCounter = 0
	indents = "" #adds \t everytime we see something in INDENTKEY.. removes \t everytime we hear "end brace" (i.e. end of if, end of for loop, end of while loop... in all cases we want to go to the next highest local indentation i.e. one less)
	# do and logic to fix nested if problem i.e. if we are in a boolean expression (limit to if for now, can make a func later to let it be used generally) then it is used to join two boolean expressions... else, it is just being used to start a new line. fix nested if "then" problem.
	#one potential fix is to force the user to say "end" when they are finished saying their sentence completely. then i wouldn't need to account for major problems like 'then' hanging
	#possibly treat "and"'s that occur at the very end of a sentence to be significant? i.e. and if ____ then ____ and,,,,, print___   vs and if ___ then ____,,,,, and print___

	def __init__(self):
		pathToName = 'Files/WorkingFile/FileName.txt'
		with open(pathToName, 'r') as reader:
			self.fileName = os.path.join('Files', reader.read() + '.c')

	def changeOutFile(self): #runs everytime the object is called (incase the user changes files)
		pathToName = 'Files/WorkingFile/FileName.txt'
		with open(pathToName, 'r') as reader:
			self.fileName = os.path.join('Files', reader.read() + '.c')

	def newInput(self, string):
		self.string = string
		# common words pre-naming var are "called" and "named".... they fit between dataType and varName most of the time so we can just remove it. we search for these words, if they don't show up default is just around =. also , search after any operator for any "to" as in let this equal to, or if a equal euqal to b since "to" is fairly common after equals
		self.string = self.string.replace(" called ", " ")
		self.string = self.string.replace(" named ", " ")
		self.string = self.string.replace(" to ", " ")
		self.string = self.string.replace(" is ", " ")
		self.string = self.string.replace(" by ", " ")
		self.string = self.string.replace(" be ", " ")
		self.currentArray = self.string.split(" ")
		self.coordinator() # the moment you detect new input, you want to put it to the text file. so run coordinator to normalize/translate/determine which function to use to check if the input is valid go on from there

#standard is add ; to current line, and also add newlines and tab to the end of each line for the next line.
	def coordinator(self):
		oldLength = len(self.stringArray)
		if self.string == "end code":
			if self.indentCounter == 0:
				self.stringArray.append(";")
			else: 
				while self.indentCounter > 0:
					self.endCreator()
		if self.indentCounter > 0 and (self.string == "end brace" or self.string == "end loop" or self.string == "end if"):
			self.endCreator()
		else:
			self.translate()
			self.normalize()
			self.stringArray.extend(self.currentArray)
			 # iterator is the index of word in stringArray, j is the index of the found keyword in the KEYWORD array, and k is the index of the next found keyword in stringArray
			length = len(self.stringArray)
			while self.iterator < length: # go through every single word. if i see a keyword, then we decide what to do then.
				k = len(self.stringArray)
				if not set(self.KEYWORDS).isdisjoint(self.stringArray[self.iterator+1:]):
					for l in range(self.iterator+1, len(self.stringArray)):
						if self.stringArray[l] in self.KEYWORDS:
							k = l
							break
				if self.stringArray[self.iterator] in self.INDENTKEYS:
					self.indentCounter+=1 
					self.indents+='\t'
				if self.stringArray[self.iterator] == "and": #and implies end of statement MIGHT ASWELL JUST SWITHC TO ==and
					if '\n' not in self.stringArray[self.iterator+1]:
						self.stringArray[self.iterator] = ';\n' + self.indents
					else:
						del self.stringarray[self.iterator]
						self.iterator-=1
				elif self.stringArray[self.iterator] in self.DATAKEYWORDS and self.stringArray[self.iterator+1] in self.INDEPDATALIST: # matches to data type sections
					if self.stringArray[:self.iterator] and "{" not in self.stringArray[self.iterator-1] and "}" not in self.stringArray[self.iterator-1]:
						self.stringArray.insert(self.iterator, ";\n" + self.indents) #WE DON'T NEED TO DO ANYTHIGN TO DATAKEYWORDS BECAUSE THEY ONLY NEED FORMATTING FROM 'AND' and co.
						self.iterator+=1
						length+=1
				elif self.stringArray[self.iterator] in self.IFKEYWORDS: # then we found a matching keyword to the IF's section. note that if we see else if or else, we assume that it is for the most recent one.
					if self.stringArray[self.iterator] == "if": # try combining common stuff
						self.stringArray[self.iterator] += " ("
					elif self.stringArray[self.iterator] == "elseif": # so end of last statement
						self.stringArray[self.iterator] = ";\n" + self.indents[:-1] + "} else if ("
					elif self.stringArray[self.iterator] == "else": # same
						self.stringArray[self.iterator] = ";\n" + self.indents[:-1] + "} else {\n" + self.indents
					elif self.stringArray[self.iterator] == "then":
						self.stringArray[self.iterator] = ") {\n" + self.indents
				elif self.stringArray[self.iterator] == "print": # check if any vars. if yes, replace the var in the stringArray with the %sign, then add comma to end and do corressponding order of w/e
					self.stringArray[self.iterator] = 'printf("'
				elif self.stringArray[self.iterator] == "while":
					self.stringArray[self.iterator] += " ("
					self.stringArray.insert(k, ") {\n" + self.indents) # fix. what if i have not a keyword as my next line, but a+=b or something li that? ALSO FIX: WHY ARE IF STATEMTNS APPEARING TWICE??
					length+=1
				elif self.stringArray[self.iterator] == "!" and self.stringArray[self.iterator+1] != "=": #then it is for a boolean expr
					self.stringArray[self.iterator] += "("
					self.stringArray.insert(k, ")")
					length+=1
				self.iterator+=1
		with open(self.fileName, 'a') as output:
			output.write(self.arrayToString(self.stringArray[oldLength:]))

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
		#print(self.currentArray)

		#note: if we ever see the word "it" or 'which', we assume it only follows from after the declaration of a variable because there is no other case where "it" isn't ambiguous. so if we see it, we should also see an "equals" soon. 
		#what we have: type - var name -....- it-..-equals.. but what if type-varname-equals? then we just check if immediately next to var name is equals. if not, we check if there is an "it" in the rest of the string. if not, then assume no init
		#note we asusme that they are initializing and declaring on one line if they want to. i.e. if they have and let it equals to on a different line, we don't accept it
		#filtering out key stuff from var declaration/initialization
		if "or" in self.currentArray or not set(self.DATAKEYWORDS).isdisjoint(self.currentArray): #note that this iterates through the entire array so we can just check all outlier cases here (i.e. including the "> or ="/"< or =" case)
			j=0
			while j < len(self.currentArray):
				if self.currentArray[j] == "or" and self.currentArray[j+1] == "=" and (self.currentArray[j-1] == "<" or self.currentArray[j-1] == ">"):
					del self.currentArray[j]
					j-=1
				elif self.currentArray[j] in self.DATAKEYWORDS:
					# if equals is right beeside the variable already then just coninute searching (can't leav eit out otherwise we invoke else)
					self.DATAWHITELIST.append(self.currentArray[j])
					if "=" in self.currentArray[j:] and self.currentArray[j+2] == "=": 
						self.NAMEWHITELIST.append(self.currentArray[j+1])
						if self.currentArray[j] == "char":
							self.charInit(j+3)
						else:  #we see int or double, send everything after "=" to the next operator (otherwise to the next keyword (account for "and"'s by checking if the next thing is a number?)) to mini calculator class
							self.numInit(j+3)
						j+=1
						continue
					elif ("it" in self.currentArray[j:] or "which" in self.currentArray[j:]): # equals is somewhere in the distance, but is the first equals to show up and "it" implies that the variable is getting inited
						if "=" in self.currentArray[j+1:]: # 'it' refers to the var. if we have = somewhere in the distance then it implies that the user wants to initialize
							self.NAMEWHITELIST.append(self.currentArray[j+1])
							if self.currentArray[j] == "char":
								self.charInit(j+self.currentArray[j:].index("=")+1)
							else:
								self.numInit(j+self.currentArray[j:].index("=")+1)
							inc = self.currentArray[j:].index("=")-1
							self.currentArray = self.currentArray[:j+2] + self.currentArray[j+self.currentArray[j:].index("="):] #we require the [j:] because what if multi var declaration
							j+=inc
						else: #otherwise, the it is just to name the variable 
							self.NAMEWHITELIST.append(self.currentArray[j+self.currentArray[j:].index("it")+1])
							self.INDEPDATALIST.append(self.currentArray[j+self.currentArray[j:].index("it")+1])	
					else: # there is no equals anywhere that belongs to the datatype... so we did not init
						self.NAMEWHITELIST.append(self.currentArray[j+1])
						self.INDEPDATALIST.append(self.currentArray[j+1])
				j+=1

		self.currentArray.append(None) #to ensure normalizeOps doesn't crash. note this will automatically get filtered out eventually
		self.currentArray.append(None)
		# stores every NECCESSARY word into a different array, then let self.currentArray = that array in the end (the "normalizing/standardizing" part)
		tempArray = []
		ind = 0
		while (ind < len(self.currentArray)): 
			# if we see 'and', then we check if the stringArray or tempArray actually contians anything yet for us to join. if not, do nothing (i.e. don't append and). if there is, then we check if the next thing that shows up is a) a boolean expression or b) a keyword/normal operator. if the first ting that hsows up is aboolean expression, then we use && and don't need to check if the backwards case is also boolean because it is assumed (there is not other reason for the user to use and (boolean) if there isn't a boolean already preceding the and)
			if self.currentArray[ind] == "endprint":
				self.PRINTMODE = False
				self.ENDOFPRINTFORMAT+=")"
				tempArray.append(self.ENDOFPRINTFORMAT)
				self.ENDOFPRINTFORMAT='"'
				del self.currentArray[ind] # note that endprint has served its purpose so we can just delete it. this will prevent issues furthe ron down thee line (i.e. in coordinate, indexing the next keyword. we don't wnat to index this as the next)
				ind-=1
			elif self.PRINTMODE == True and self.currentArray[ind] is not None: #this has the highest priority (after endprint, as we need to terminate this somehow if print gets run) because we may see keywords while we are printing. NOTE THAT WE DON'T WANT TO INCLUDE ANY NULL'S INTO OUR STRING
				# we are assuming that if they try printing anything and that 'thing' shares a name with a prev declared var, then they want the var
				if self.currentArray[ind] in self.NAMEWHITELIST:
					self.ENDOFPRINTFORMAT += ","
					self.ENDOFPRINTFORMAT += self.NAMEWHITELIST[self.NAMEWHITELIST.index(self.currentArray[ind])]
					self.currentArray[ind] = self.FORMATTERS[self.DATAKEYWORDS.index(self.DATAWHITELIST[self.NAMEWHITELIST.index(self.currentArray[ind])])] #replacing the word with the corresonding %thing
				tempArray.append(self.currentArray[ind])
			elif self.currentArray[ind] == "print":
				self.PRINTMODE = True
			if self.currentArray[ind] == "and": # i.e. keyword is and. three types of ands: one for new line, one for the actual operator, and one that is useless. looking 
				#CONSIDER: WHY NOT JUST MAKE AN ARRAY OF ACCEPTED VALUES FOR AND AND PUT IT HERE INSTEAD?
				#NOTE: THIS IS CURRENTLY FLAWED I.E. DOESN'T WORK FOR PRINT B/C WHEN IT GETS AROUND TO CHECKING THIS (if we have and on a new line that is, and on the same line is fine), PRINT HAS BECOME PRINT(", WE CAN FIX EITHER BY CONCATENATING (" TO THE NEXT WORD OR BY CHANGING THE CONDITION TO PRINT("
				#ANOTHER POSSIBLE FIX IS TO JUST EXCLUDE IT ENTIRELY, BUT IS THIS EFFICIENT?
				#if not ((set(self.stringArray).isdisjoint(self.SYMBOLS) and set(self.stringArray).isdisjoint(self.IFKEYWORDS) and set(self.stringArray).isdisjoint(self.PRINTKEYWORDS)) and (set(tempArray).isdisjoint(self.SYMBOLS) and set(tempArray).isdisjoint(self.IFKEYWORDS) and set(tempArray).isdisjoint(self.PRINTKEYWORDS))): #note that we only need to check for keywords that are not data types because we don't want and's to have anything to do with them
					for j in range(ind, len(self.currentArray)):
						if self.currentArray[j] in self.BOOLSYM and (self.currentArray[j+1] in self.BOOLSYM or self.currentArray[j] == "<" or self.currentArray[j] == ">"):
							print(self.currentArray)
							tempCumulative = self.arrayToString(self.stringArray).split(" ")
							tempCumulative.extend(tempArray)
							i = len(tempCumulative)-1
							while i >= 0:
								if tempCumulative[i] in self.BOOLSYM and (tempCumulative[i-1] in self.BOOLSYM or tempCumulative[i] == "<" or tempCumulative[i] == ">"):
									tempArray.append("&&")
									break
								elif tempCumulative[i] in self.IFKEYWORDS or tempCumulative[i] in self.PRINTKEYWORDS or tempCumulative[i] in self.SYMBOLS:
									tempArray.append("and")
									break
								i-=1
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
			elif (self.currentArray[ind] in self.KEYWORDS or (self.currentArray[ind] in self.INDEPDATALIST and self.currentArray[ind+1] != "=")): #cases of remaining keywords or var names that aren't specifically handled. i.e. we for certain want to store just the current index into the new array
				# DO I NEED THE "THEN NOT IN SELF.CURRENTARRAY[I]"?   #if (self.currentArray[ind] == "if" and ("then" not in self.currentArray[ind:] or "if" in self.currentArray[ind+1:] and "then" not in self.currentArray[ind:ind+self.currentArray[ind+1:].index("if")+1])): #we don't want to count if's twice UNLESS there is a 'then' in between the two... note that we MUST ignore the correct one (i.e. the first one if we have 'create an if statement and basically if....') because if we don't, and may possibly not work correctly
				if (self.currentArray[ind] == "if" and "if" in self.currentArray[ind+1:] and "then" not in self.currentArray[ind:ind+self.currentArray[ind+1:].index("if")+1]) or (self.currentArray[ind] == "while" and "while" in tempArray and tempArray[-1] == "while"): #we don't want to count if's twice UNLESS there is a 'then' in between the two... note that we MUST ignore the correct one (i.e. the first one if we have 'create an if statement and basically if....') because if we don't, and may possibly not work correctly
					ind+=1
					continue
				else:
					tempArray.append(self.currentArray[ind])
			elif (self.currentArray[ind] in self.SYMBOLS):
				if self.currentArray[ind] == "!" and self.currentArray[ind+1] != "=":
					tempArray.append("!")
				else:
					tempArray.append(self.currentArray[ind-1]) #we are assuming that we USUALLY get an operator that is binary. so store the thing before the operator
					end = self.normalizeOps(ind)
					for j in range(ind, end+1): #i.e. between the current operator and thte var adjacent to the last relevant local operator
						tempArray.append(self.currentArray[j]) #store everything in between
					ind = end #after i++, start searching at indice after the last relevant thing
			ind+=1 #start seraching for the next term
		self.currentArray = tempArray
		tempArray = None

	def endCreator(self):
		if '\n' in self.stringArray[-1]: #i.e. if we already have tab values, then we also have semicolons. else, no tab values probably implies no semicolons (by the standard).. we may have the else case for stuff like if -> then-> and ->end brace->if
			self.stringArray[-1] = self.stringArray[-1][:-1] #remove one tab to properly place brace
			self.stringArray.append("}\n" + self.indents[:-1])
		else:
			self.stringArray.append(";\n" + self.indents[:-1] + "}\n" + self.indents[:-1])
		self.indentCounter-=1
		self.indents = self.indents[:-1]

	def charInit(self, indOfValue):
		temp = "'"
		temp+= self.currentArray[indOfValue]
		temp+= "'"
		self.currentArray[indOfValue] = temp

	def numInit(self, startIndOfValue):
		calc = MiniCalculator()
		preInputPhraseInd = startIndOfValue
		inputPhrase = ""
		i = startIndOfValue
		while i < len(self.currentArray):
			if self.currentArray[i] in self.SYMBOLS or (i+1 < len(self.currentArray) and self.currentArray[i] == "and" and self.currentArray[i+1] not in calc.ONES and self.currentArray[i+1] not in calc.TENS): #note that we are guarenteed one of two things to terminate i.e. start deecoding the next number: an operator or 'and' that isn't used for describing a number
				if inputPhrase != "":
					calc.newInput(inputPhrase)
					temp = self.currentArray[i:]
					dec = i-preInputPhraseInd-1
					self.currentArray = self.currentArray[:preInputPhraseInd]
					self.currentArray.append(str(calc.decode()))
					self.currentArray.extend(temp)
					i-=dec
				if self.currentArray[i] in self.SYMBOLS:
					preInputPhraseInd = i+1
					inputPhrase = ""
				elif i+1 < len(self.currentArray) and self.currentArray[i] == "and" and self.currentArray[i+1] not in calc.ONES and self.currentArray[i+1] not in calc.TENS:
					break		
			elif self.currentArray[i] not in self.NAMEWHITELIST:
				inputPhrase+=str(self.currentArray[i] + " ")
			if i+1 == len(self.currentArray):
				calc.newInput(inputPhrase)
				temp = self.currentArray[i:]
				self.currentArray = self.currentArray[:preInputPhraseInd]
				self.currentArray.append(str(calc.decode()))
				self.currentArray.extend(temp)
				break
			i+=1

	def keywordConcatenator(self, result, keyword1, keyword2):
		if keyword1 in self.currentArray: 
			for i in range(self.currentArray.index(keyword1), len(self.currentArray)-1):
				if self.currentArray[i] == keyword1 and self.currentArray[i+1] == keyword2: # i.e. we have else in one index, and if in the other index. user probably wnats else if
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
		print(array)
		string = ""
		for i in range(len(array)): 
			if (i+1 != len(array) and (';' in array[i+1] or ';' in array[i] or ")" in array[i+1])) or i is len(array) - 1 or (array[i] in self.SYMBOLS and array[i+1][0] in self.SYMBOLS) or "(" in array[i] or "{" in array[i] or "}" in array[i]: # very first condition: if this is the last thing, we likely DO NOT want an extrra space. reasons: user is finished line, so extra space doesnt matter. but if user does not finish line, some complications: end with "plus", start with "equals"... once again another conflict is "equals" and "not" -> I THINK SEPARATING THIS IS EASY. JUST IGNORE CONCATENATION WHEN YOU SEE =!, AS ! BELONGS TO THE NEXT... BUT DON'T IGNROE != BECAUSE IT ALWAYS BELONGS TO =.. if this is not the last iterations, and if current and next element are operators, want to likely concatenate 
				string += array[i]
			else:
				string += array[i] + " "
		return string

if __name__ == "__main__": #instead we have 'code mode' or something of the sort, which initializes a functions object. then opens files on request, and when does, the person will say finished code mode or osmething, which closes the files
	funcs = Functions() # new functions object w/o intialization
	#funcs.newInput()
	#funcs.newInput("create a while loop where the condition is while a is less than b")
	#funcs.newInput("if not a equals equals b then c plus d and make another while loop where while a is greater than b")
	#funcs.newInput("if not a equals equals b then c plus d and make another while loop where while a is greater than b")
	#funcs.newInput("a plus b and a plus b and c plus d and if then a plus b else a plus b and a plus b")
	#funcs.newInput("and a plus b and a plus b")

#	funcs.newInput("give me an integer called a equal to a plus two hundred and three divided by three and also create a char b equals g and a char called c and let it equal to r and a double called d and let it equal to a plus b plus c plus thirty three point four five")
#	funcs.newInput("and if a equals equal b then print hello end print and if then a plus b and if then a plus b and if then a minus b")
#	funcs.newInput("create a new double and call it bestfriend")
#	funcs.newInput("and print hello bestfriend there end print")
#	funcs.newInput("end if")
#	funcs.newInput("and add a break")
#	funcs.newInput("end if")
#	funcs.newInput("add a break and add a break and print helloooooo a")
#	funcs.newInput("there end print and print why am i still alive")
#	funcs.newInput("end print")
#	print(funcs.NAMEWHITELIST)

	#calc = MiniCalculator("three three two point zero zero one")
	#print(calc.decode())
	#calc = MiniCalculator("two hundred and twenty two thousand two hundred and twenty two point two zero two two")
	#print(calc.decode())

	#funcs.newInput("give me an new integer named ABC and it equals three and also give an integer named CDF and it equals four")
	#funcs.newInput("now and uhh create an if statement where uh if ABC equals equals CDF or A is greater than or equals to B then")
	#funcs.newInput("i want to create an int G equals five and G mod equals two")
	#funcs.newInput("else if ABC greater than CDF then G plus plus")
	#funcs.newInput("else G minus minus")
	#funcs.newInput("if G equals equals ten and H equals equals eleven then")
	#funcs.newInput("if G minus G equals equals ABC then ABC plus DEF")
	#funcs.newInput("end if")
	#funcs.newInput("now in here i want a plus b")
	#funcs.newInput("now we have else if G plus G equals equals Z then G plus equals G")	
	#print(funcs.stringArray)
	#difficult to filter out "and" and "then" w/o making mistakes

	# TEST THE BELOW AGAIN ONCE I IMPLEMENT THE WHITE LIST
#	funcs.newInput("give me a new integer f equals ten")
#	funcs.newInput("and give me a new integer x")
#	funcs.newInput("and give me a new integer y and it equals ten")
#	funcs.newInput("and give me a new integer called z")
#	funcs.newInput("and let z be equal to ten") #what if user only wants to create f but not initialize?
#	funcs.newInput("and let z be equal to ten")
#	funcs.newInput("and give me a new integer called h and let it be equal to ten")

#	funcs.newInput("if a equals equals b then")
#	funcs.newInput("create a new integer a which is equal to three")
#	funcs.newInput("and make an integer called b equals ten")
#	funcs.newInput("else if a not equals b then")
#	funcs.newInput("if a equals equals ten then")
#	funcs.newInput("a mod equals b")
#	funcs.newInput("and c plus equals d")
#	funcs.newInput("and make an integer called c equals two")
#	funcs.newInput("and three plus four")
#	funcs.newInput("and make an integer called d and let it be equal to two")
#	funcs.newInput("and make an integer called e equals two") ###### if we stop here, we will see that this line automatically adds its own semi colon. ( if we kept the and condition \n and the coordinator for datatypes)
#	funcs.newInput("else if a equals equals b then a plus equals b and make an integer called f")
#	funcs.newInput("and three plus four else a plus equals b and a plus b")
#	funcs.newInput("end if")
#	funcs.newInput("end if")
#	print(funcs.INDEPDATALIST)	

	#funcs.newInput("end code")
	#funcs.newInput("give me a new integer called z")
	#funcs.newInput("end code")

	print("MY NEW THING:")
	print(funcs.arrayToString(funcs.stringArray))