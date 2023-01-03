package script;

import entities.ECS;
import entities.Entity;

public interface ScriptEntity {
	void start(ECS ecs, Entity entity);
	void tick(ECS ecs, Entity entity);
}
