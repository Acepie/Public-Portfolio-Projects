bird.o: bird.cpp bird.h obstacle.h
	g++ -c -std=c++0x -Wall bird.cpp -lglut -lGL -lGLU -lGLEW -lm
	
spike.o: spike.cpp spike.h obstacle.h
	g++ -c -std=c++0x -Wall spike.cpp -lglut -lGL -lGLU -lGLEW -lm
	
GameWorld.o: GameWorld.cpp GameWorld.h level.h
	g++ -c -std=c++0x -Wall GameWorld.cpp -lglut -lGL -lGLU -lGLEW -lm
	
level.o: level.cpp level.h obstacle.h obstacle.h bird.h spike.h
	g++ -c -std=c++0x -Wall level.cpp -lglut -lGL -lGLU -lGLEW -lm
	
World.o: World.cpp World.h
	g++ -c -std=c++0x -Wall World.cpp -lglut -lGL -lGLU -lGLEW -lm
	
game: bird.o spike.o GameWorld.o level.o World.o
	g++ -std=c++0x -Wall main.cpp bird.o spike.o GameWorld.o level.o World.o -o game -lglut -lGL -lGLU -lGLEW -lm
	
clean:
	rm bird.o spike.o GameWorld.o level.o World.o game
