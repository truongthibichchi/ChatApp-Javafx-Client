package controller;

import connection.User;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class CellRendererInChat implements Callback<ListView<User>, ListCell<User>> {
    @Override
    public ListCell<User> call(ListView<User> p) {

        ListCell<User> cell = new ListCell<User>() {

            @Override
            protected void updateItem(User user, boolean bln) {
                super.updateItem(user, bln);
                setGraphic(null);
                setText(null);
                if (user != null) {
                    VBox vBox = new VBox();

                    Text name = new Text(user.getNickname());

                    ImageView statusImageView = new ImageView();
                    Image statusImage = new Image(getClass().getClassLoader().getResource("images/" + user.getStatus().toString().toLowerCase() + ".png").toString(), 16, 16, true, true);
                    statusImageView.setImage(statusImage);

                    Image image = new Image(getClass().getClassLoader().getResource("images/avatars/" + user.getUsername().toLowerCase() + ".png").toString(), 50, 50, true, true);
                    Circle circle = new Circle();
                    circle.setRadius(20);
                    circle.setFill(new ImagePattern(image));
                    vBox.getChildren().addAll(statusImageView, circle, name);

                    vBox.setAlignment(Pos.CENTER);

                    setGraphic(vBox);

                }
            }
        };
        return cell;
    }
}
