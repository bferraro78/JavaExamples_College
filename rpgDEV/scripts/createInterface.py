f = open("../dude.java"  ,"r")

stateNames = []

stateNames = f.readlines()

stateNames = map(lambda s: s.split("{"), stateNames)


header = open('../HeroInterface.java', 'w')

header.write("package rpg;\n");

header.write("public interface HeroInterface { \n");

for line in stateNames:
	if "protected" in line[0]  or "private" in line[0]:



		if line[0][-1] != ";" or line[0][-2] != " ":
			print line[0][:-1]

			line[0] = line[0][:-1]
			header.write(line[0] + ";\n")
		else:
			header.write(line[0] + "\n")
		



header.write("}")