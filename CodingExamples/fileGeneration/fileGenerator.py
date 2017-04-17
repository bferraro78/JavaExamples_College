### 4/15/17 - Benjamain Ferraro
##Read states from text file, create a .h (struct) and .c file (array)

### ~~ PYTHON ~~ ###


f = open("beano.txt", 'r')

stateNames = []

stateNames = f.readlines()
	# - Contains each line from OG textfile



# Chomp all \n characters
stateNames = map(lambda s: s.strip('\n'), stateNames)

# # Create files for writing
header = open('name.h', 'w')
body = open('name.c', 'w')

nameOfStruct = ""

for line in stateNames:
	line = line.split()
	if line[0] == 'M': # new struct
		# ### Write header file
		nameOfStruct = line[1]
		header.write("extern const char* " + nameOfStruct + "_names[];\n")
		header.write("typedef enum {\n")
		body.write("const char* " + nameOfStruct + "_names[] = {\n")
	elif line[0] == 'F' : # continue creating struct
		header.write("char* " + line[1] + ',\n')
		body.write("\"" + line[1] + '\"\n')
	elif line[0] == 'E': # end struct
		header.write(" } " + nameOfStruct + ";\n\n")
		body.write("};\n\n")


