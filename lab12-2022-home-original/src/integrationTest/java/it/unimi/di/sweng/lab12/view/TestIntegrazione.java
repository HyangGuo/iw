package it.unimi.di.sweng.lab12.view;


import static it.unimi.di.sweng.lab12.Main.MAX_FOOD;
import static org.assertj.core.api.Assertions.assertThat;

import it.unimi.di.sweng.lab12.model.GroceryListModel;
import it.unimi.di.sweng.lab12.presenter.CommandPresenter;
import it.unimi.di.sweng.lab12.presenter.DisplayPresenter;
import it.unimi.di.sweng.lab12.presenter.PresenterInput;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;


public class TestIntegrazione extends ApplicationTest {


  private InputView input;
  private DisplayView display;
  private DisplayView display2;
  private CommandView[] command;

  @Override
  public void start(Stage primaryStage) {

    primaryStage.setTitle("Grocery List");

    command = new CommandView[2];

    input = new InputView();
    display = new DisplayView(MAX_FOOD, "Cose da comprare");
    display2 = new DisplayView(MAX_FOOD, "Cose comprate");

    GridPane gridPane = new GridPane();
    gridPane.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
    gridPane.setPadding(new Insets(10, 10, 10, 10));

    gridPane.add(input, 0, 0);
    GridPane.setColumnSpan(input, GridPane.REMAINING);
    for (int i = 0; i < 2; i++) {
      command[i] = new CommandView("Buy", MAX_FOOD, "Compratore#" + i);
      gridPane.add(command[i], i, 1);
    }

    gridPane.add(display, 0, 2);
    gridPane.add(display2, 1, 2);

    //TODO da completare creando modello e presenter e collegandoli opportunamente

    GroceryListModel model = new GroceryListModel();
    CommandPresenter commandPresenter = new CommandPresenter(model,command[0],command[1],MAX_FOOD);
    DisplayPresenter displayPresenter = new DisplayPresenter(model,display,display2,MAX_FOOD);
    PresenterInput presenterInput = new PresenterInput(model,input,MAX_FOOD);


    Scene scene = new Scene(gridPane);
    primaryStage.setScene(scene);
    primaryStage.show();

    //HINT: utile dopo aver definito model per inizializzare viste
    model.notifyObservers();
  }

  @Test
  public void testSomething() {
    // FIG 1
    assertThat(command[0].get(0)).startsWith("---");
    assertThat(command[1].get(0)).startsWith("---");

    // FIG 2
    doubleClickOn(input.text);
    write("mele");
    press(KeyCode.ENTER);
    release(KeyCode.ENTER);
    doubleClickOn(input.num);
    write("6");
    press(KeyCode.ENTER);
    release(KeyCode.ENTER);
    clickOn(input.addButton);

    assertThat(command[0].get(0)).startsWith("mele");
    assertThat(command[1].get(0)).startsWith("mele");
    assertThat(display.get(0)).startsWith("mele").endsWith("6");


    // FIG 3
    clickOn(command[1].buttons[0]);
    clickOn(command[0].buttons[0]);
    clickOn(command[0].buttons[0]);

    doubleClickOn(input.text);
    write("pere");
    press(KeyCode.ENTER);
    release(KeyCode.ENTER);
    doubleClickOn(input.num);
    write("4");
    press(KeyCode.ENTER);
    release(KeyCode.ENTER);
    clickOn(input.addButton);

    clickOn(command[1].buttons[0]);
    clickOn(command[1].buttons[0]);

    assertThat(display.get(0)).startsWith("mele").endsWith("1");
    assertThat(display.get(1)).startsWith("pere").endsWith("4");

    assertThat(display2.get(0)).startsWith("mele").endsWith("5");

    // FIG.4
    //TODO proseguire nella interazione e controllare esito

    clickOn(command[1].buttons[0]);
    assertThat(display.get(0)).startsWith("pere").endsWith("4");
    assertThat(display.get(1)).startsWith("---");


  }
}
