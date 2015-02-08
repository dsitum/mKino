#!/usr/bin/python3.3

import random

s = "Insert into projekcijefilmova (dvorana, film, vrijemePocetka, cijena) values "

for k in range(9, 24, 2):  # 8 puta. vrijeme
	for i in range(2, 22): # svi filmovi
		svakiFilmUXDvorana = random.randint(10, 15) #uključive obje granice
		iskoristeneDvoraneUTerminu = set()
		for j in range(svakiFilmUXDvorana):
			# biramo dvoranu
			dvorana = random.randint(1, 80)
			while ((dvorana > 26 and dvorana < 35) or (dvorana > 49 and dvorana < 58) or (dvorana > 65 and dvorana < 76) or (dvorana in iskoristeneDvoraneUTerminu)):
				dvorana = random.randint(1, 80)
			iskoristeneDvoraneUTerminu.add(dvorana)
			s += "({dv}, {film}, '2014-01-{dan} {sat}:{minuta}:00', {cijena}), ".format(dv=dvorana, film=i, dan=random.randint(1, 10), sat=k, minuta=str(random.randint(0,1)*30).zfill(2), cijena=25+random.randint(0,2)*5)

with open('sve.sql', 'w') as f:
	f.write(s)
