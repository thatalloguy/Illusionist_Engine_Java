package script;

import Editor.Game;
import entities.ECS;

public interface Script {
	void start(ECS ecs, Game game);
	void tick(ECS ecs, Game game);
}
