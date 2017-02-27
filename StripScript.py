import hashlib
import base64
import sys


def hashEncode(pwd):
     new_pwd = 'CMSC414' + pwd + 'Fall16'
     k = hashlib.sha256()
     k.update(new_pwd)
     
     retv = base64.b64encode(k.digest())
     return retv



ret = []

with open(sys.argv[1], 'r') as testPWD:
     array = []
     for line in testPWD:
          array.append(line)
     

for b in array:
     b = b.strip('\n')
     b = b.strip('\r')
     value = hashEncode(b)
     value = value.strip('\n')
 
     f = open(sys.argv[2], 'r')
     for l in f:
          l = l.strip('\n')
          if value == l:
               if b not in ret:
                    print 'match: ' + b
                    ret.append(b)
f.close()



print ret
print len(ret)

f3 = open('cracked.txt', 'w')

for bean in ret:
     f3.write(bean+'\n')

f3.close()
