package fr.hadriel.state;

/**
 *
 * @author glathuiliere
 */
public interface IState {

    public void onEnter();

    public void onUpdate(float delta);

    public void onRender();

    public void onLeave();
}