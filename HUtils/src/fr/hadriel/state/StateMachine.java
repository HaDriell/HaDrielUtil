package fr.hadriel.state;

import java.util.Stack;

/**
 *
 * @author glathuiliere
 */
public class StateMachine<State extends IState> {

    private Stack<State> stack;

    public StateMachine() {
        this.stack = new Stack<>();
    }

    public State current() {
        return stack.isEmpty() ? null : stack.peek();
    }

    public void push(State state) {
        //avoid EmptyStackException when the first state is pushed
        if(!stack.isEmpty())
            stack.peek().onLeave();
        stack.push(state);
        state.onEnter();
    }

    public void pop() {
        stack.pop().onLeave();
        //avoid EmptyStackException when the last State is poped
        if(!stack.isEmpty())
            stack.peek().onEnter();
    }

    public void set(State state) {
        if(!stack.isEmpty())
            stack.pop().onLeave();
        stack.push(state);
        state.onEnter();
    }

    public void update(float dt) {
        State state = current();
        if(state != null) {
            state.onUpdate(dt);
        }
    }
}