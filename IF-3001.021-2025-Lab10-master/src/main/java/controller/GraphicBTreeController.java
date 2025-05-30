package controller;

import domain.BST;
import domain.BTree;
import domain.BTreeNode;
import domain.TreeException;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import util.Utility;

public class GraphicBTreeController {
    @javafx.fxml.FXML
    private Text txtMessage;
    @javafx.fxml.FXML
    private Pane mainPain;
    @javafx.fxml.FXML
    private Button tourInfoButton1;
    @javafx.fxml.FXML
    private Button levelsButton;
    @javafx.fxml.FXML
    private Button tourInfoButton;
    @javafx.fxml.FXML
    private Pane treePane;
    @javafx.fxml.FXML
    private AnchorPane AP;
    @javafx.fxml.FXML
    private Button randomizeButton;
    private BST bst;


    // Constantes para los niveles
    private static final double NODE_RADIUS = 20;
    private static final double VERTICAL_SPACING = 80; // Espacio entre niveles
    private static final double HORIZONTAL_INITIAL_SPACING = 300; // Espacio horizontal inicial para los hijos de la raíz
    private static final double HORIZONTAL_REDUCTION_FACTOR = 0.6; // Cuánto se reduce el espacio horizontal por nivel
    private static final double LEVEL_LINE_OFFSET_X = 50; // Desplazamiento X para las líneas de nivel
    private static final double LEVEL_TEXT_OFFSET_X = 20; // Desplazamiento X para los números de nivel
    private static final double START_Y = 50; // Posición Y inicial para el nodo raíz

    public void initialize() {
        // Crear árbol binario con valores aleatorios
        bst = new BST();

        for (int i = 0; i < 20; i++) {
            bst.add(Utility.random(50));
        }

        drawTree();


    }

    private void drawTree() {
        treePane.getChildren().clear(); // Limpiar el panel antes de dibujar

        if (!bst.isEmpty()) {

            drawBTreeNode(bst.getRoot(), treePane.getPrefWidth() / 2, START_Y, HORIZONTAL_INITIAL_SPACING, 0);

        }
    }

    private void drawBTreeNode(BTreeNode node, double x, double y, double hSpace, int level) {

        if (node == null) return;

        // Dibujar el nodo
        Circle circle = new Circle(x, y, NODE_RADIUS, Color.LIGHTBLUE);
        circle.setStroke(Color.BLACK);

        Text text = new Text(x - 10, y + 5, String.valueOf(node.data));
        text.setFont(new Font(12));

        treePane.getChildren().addAll(circle, text);

        // Dibujar hijos recursivamente
        double childY = y + VERTICAL_SPACING;
        double childHSpace = hSpace * HORIZONTAL_REDUCTION_FACTOR;

        // Hijo izquierdo
        if (node.left != null) {
            double leftX = x - hSpace / 2;
            Line line = new Line(x, y + NODE_RADIUS, leftX, childY - NODE_RADIUS);
            treePane.getChildren().add(line);
            drawBTreeNode(node.left, leftX, childY, childHSpace, level + 1);
        }

        // Hijo derecho
        if (node.right != null) {
            double rightX = x + hSpace / 2;
            Line line = new Line(x, y + NODE_RADIUS, rightX, childY - NODE_RADIUS);
            treePane.getChildren().add(line);
            drawBTreeNode(node.right, rightX, childY, childHSpace, level + 1);
        }

    }

    private void drawLevels() throws TreeException {
        int treeHeight = bst.height(); // Obtener la altura máxima del árbol
        double paneWidth = treePane.getPrefWidth();

        for (int i = 0; i <= treeHeight; i++) {
            double y = START_Y + (i * VERTICAL_SPACING); // Calcular la posición Y para cada nivel

            // Dibujar línea horizontal
            Line levelLine = new Line(LEVEL_LINE_OFFSET_X, y, paneWidth - LEVEL_LINE_OFFSET_X, y);
            levelLine.setStroke(Color.LIGHTGREEN); // Color verde para las líneas de nivel
            levelLine.setStrokeWidth(2); // Línea más gruesa
            treePane.getChildren().add(levelLine);

            // Dibujar número de nivel
            Text levelText = new Text(LEVEL_TEXT_OFFSET_X, y + 5, String.valueOf(i));
            levelText.setFont(new Font(16));
            levelText.setFill(Color.BLACK); // Color negro para el texto del nivel
            treePane.getChildren().add(levelText);
        }
    }

    // Método para regenerar el árbol con nuevos valores aleatorios
    @javafx.fxml.FXML
    private void regenerateTree() {
        bst.clear();
        for (int i = 0; i < 20; i++) {
            bst.add(Utility.random(50));
        }
        drawTree();
    }

    @javafx.fxml.FXML
    public void levelsOnAction(ActionEvent actionEvent) {

        treePane.getChildren().clear(); // Limpiar el panel

        if (!bst.isEmpty()) {
            try {
                drawLevels(); // Dibuja las líneas de nivel

                drawBTreeNode(bst.getRoot(), treePane.getPrefWidth() / 2, START_Y, HORIZONTAL_INITIAL_SPACING, 0);
            } catch (TreeException e) {
                txtMessage.setText("Error displaying levels: " + e.getMessage());
            }
        }
        util.FXUtility.showMessage("INFORMACIÓN", "The levels have been shown");
    }


    @javafx.fxml.FXML
    public void randomizeOnAction(ActionEvent actionEvent) {

        // Crear nuevo árbol con elementos aleatorios
        bst = new BST();
        int nodeCount = 15 + (int)(Math.random() * 10); // Entre 15 y 25 nodos

        for (int i = 0; i < nodeCount; i++) {
            bst.add(Utility.random(50)); // Valores entre 1 y 100
        }

        drawTree();

        util.FXUtility.showMessage("INFORMATION","The tree is randomized" );
    }

    @javafx.fxml.FXML
    public void tourInfoOnAction(ActionEvent actionEvent) {

        String tourInfoContent = "";
        try {
            tourInfoContent = "--- Transversal Tour Info  ---\n" + "\n" +
                    "Preorden: " + bst.preOrder() + "\n" + "\n" +
                    "Inorden: " + bst.inOrder() + "\n" + "\n" +
                    "Postorden: " + bst.postOrder() + "\n";

        } catch (TreeException e) {
            tourInfoContent = "Error: " + e.getMessage();
        }

        util.FXUtility.showTextAreaAlert("Tour Info", "Result tours", tourInfoContent);
    }

    @javafx.fxml.FXML
    public void handleScrollZoom(ScrollEvent event) {

        double zoomFactor = 1.1;
        if (event.getDeltaY() < 0) {
            zoomFactor = 1 / zoomFactor;
        }

        Pane pane = (Pane) event.getSource(); // Obtiene el Pane que emitió el evento
        pane.setScaleX(pane.getScaleX() * zoomFactor);
        pane.setScaleY(pane.getScaleY() * zoomFactor);
    }
}
