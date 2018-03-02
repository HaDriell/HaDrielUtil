package fr.hadriel;

/**
 *
 * @author glathuiliere
 */
public class TestBatchRendering {
//
//    public static Vec2 cursor = new Vec2(100, 100);
//
//    public static final Polygon CIRCLE = new Circle(8, 20);
//
//    public static class App extends GUIApplication {
//        protected void start(Window window) {
//            window.show();
//
//            Vec2 size = window.getSize();
//
//            System.out.println(" Size : " + size);
//
//            GUIApplication.runLater(() -> {
//                try {
//                    final Graphics g = new Graphics(size.x, size.y);
//                    final Texture2D texture2D = new Texture2D("Media/Teron Fielsang.png");
//                    final Timer timer = new Timer();
//
//                    window.bindCursorPos((w, xpos, ypos) -> cursor = new Vec2(xpos, ypos));
//                    window.bindWindowRender(w -> {
//                        g.begin();
//                        //Clear Rect drawing test
//                        g.settings().color(Mathf.clamp(0, 0.2f , Mathf.sin(timer.elapsed() / 10f)), 0, 0, 1);
//                        g.fillRect(0, 0, size.x, size.y);
//
//                        //Rect drawing test
//                        g.settings().color(1, 0, 0, 1);
//                        g.drawRect(cursor.x - 10, cursor.y - 10, 10, 10); // empty rect test
//                        g.fillRect(cursor.x - 10, cursor.y, 10, 10); // filled rect test
//
//                        //Lines & Curves drawing test
//                        g.drawLine(0, 0, cursor.x, cursor.y); // Line test
//                        g.drawCurve(0, 0, 0, cursor.y, cursor.x, cursor.y); // QuadraticBezierCurve test
//                        g.drawCurve(0, 0, cursor.x, 0, 0, cursor.y, cursor.x, cursor.y); // CubicBezierCurve test
//
//                        //Texture2D drawing test
//                        g.drawTexture(cursor.x, cursor.y, 32, 32, texture2D.getRegion()); // texture2D region test
//
//                        //Polygon drawing test
//                        g.settings().color(0, 0, 1, 1);
//                        g.push(Matrix3f.Translation(cursor.x - 30, cursor.y + 30));
//                        g.draw(CIRCLE);
//                        g.settings().color(0, 0, 1, 0.8f);
//                        g.fill(CIRCLE);
//                        g.pop();
//
//                        g.end();
//                    });
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//    }
//
//    public static void main(String[] args) {
//        Launcher.launch(App.class);
//    }
}