### 4/15/17 - Benjamain Ferraro
##Read states from stdin, create a .h (struct) and .c file (array)

### ~~ PYTHON ~~ ###
import sys


f = open("beano.txt", 'w')

for line in sys.stdin:
	f.write(line)

f.close()

f = open("beano.txt", 'r')

stateNames = []

stateNames = f.readlines()
	# - Contains each line from OG textfile

# Chomp all \n characters
stateNames = map(lambda s: s.strip('\n'), stateNames)

# Create files for writing
header = open('name.h', 'w')
body = open('name.c', 'w')

### Write header file
header.write("extern const char* NAME_names[];\n")
header.write("typedef enum {\n")

for line in stateNames:
	header.write(line + ',\n')

header.write(" } NAME;")

### Write body file
body.write("const char* NAME_names[] = {\n")

for line in stateNames:
	body.write("\"" + line + '\"\n')

body.write("};")