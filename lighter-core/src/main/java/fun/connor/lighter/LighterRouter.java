package fun.connor.lighter;

import fun.connor.lighter.handler.Handler;

public interface LighterRouter {
    LighterRouter get(String route, Handler handler);
    LighterRouter put(String route, Handler handler);
    LighterRouter post(String route, Handler handler);
    LighterRouter delete(String route, Handler handler);
}
