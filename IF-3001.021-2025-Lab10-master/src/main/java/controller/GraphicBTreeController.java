package controller;

import domain.BST;
import domain.BTree;
import domain.BTreeNode;
import domain.TreeException;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import util.Utility;
import domain.Tree;
import domain.AVL;

public class GraphicBTreeController {
    @javafx.fxml.FXML
    public Button BalanceButton;
    @javafx.fxml.FXML
    public RadioButton Bts;
    @javafx.fxml.FXML
    public RadioButton Avl;
    @javafx.fxml.FXML
    private Text txtMessage;
    @javafx.fxml.FXML
    private Pane mainPain;
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

    // ¡CORREGIDO! Ahora almacenará una instancia que implementa la interfaz Tree
    private Tree tree;
    private int[] treeValues; // Para almacenar los valores y poder recrear el árbol

    // Constantes para los niveles
    private static final double NODE_RADIUS = 15;
    private static final double VERTICAL_SPACING = 90; // Espacio entre niveles
    private static final double HORIZONTAL_INITIAL_SPACING = 300; // Espacio horizontal inicial para los hijos de la raíz
    private static final double HORIZONTAL_REDUCTION_FACTOR = 0.5; // Cuánto se reduce el espacio horizontal por nivel
    private static final double LEVEL_LINE_OFFSET_X = 50; // Desplazamiento X para las líneas de nivel
    private static final double LEVEL_TEXT_OFFSET_X = 20; // Desplazamiento X para los números de nivel
    private static final double START_Y = 50; // Posición Y inicial para el nodo raíz

    public void initialize() {

        ToggleGroup treeTypeGroup = new ToggleGroup();
        Bts.setToggleGroup(treeTypeGroup);
        Avl.setToggleGroup(treeTypeGroup);


        tree = new BST();
        Bts.setSelected(true); // BST seleccionado por defecto

        generateRandomTreeValues(); // Generar valores iniciales
        addValuesToTree();
        drawTree();
    }

    private void generateRandomTreeValues() {
        int nodeCount = 30; // Entre 15 y 25 nodos
        treeValues = new int[nodeCount];
        for (int i = 0; i < nodeCount; i++) {
            treeValues[i] = util.Utility.random(51); // Valores entre 1 y 50
        }
    }

    private void addValuesToTree() {
        tree.clear(); // Limpiar el árbol actual
        for (int value : treeValues) {
            tree.add(value);
        }
    }

    private void drawTree() {
        treePane.getChildren().clear(); // Limpiar el panel antes de dibujar

        if (!tree.isEmpty()) {
            // Se asume que la interfaz Tree tiene el método getRoot()
            drawBTreeNode(tree.getRoot(), treePane.getPrefWidth() / 2, START_Y, HORIZONTAL_INITIAL_SPACING, 0);
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
        // Se asume que la interfaz Tree tiene el método height()
        int treeHeight = tree.height(); // Obtener la altura máxima del árbol
        double paneWidth = treePane.getPrefWidth();

        for (int i = 0; i <= treeHeight; i++) {
            double y = START_Y + (i * VERTICAL_SPACING); // Calcular la posición Y para cada nivel

            // Dibujar línea horizontal
            Line levelLine = new Line(LEVEL_LINE_OFFSET_X, y, paneWidth - LEVEL_LINE_OFFSET_X, y);
            levelLine.setStroke(Color.LIGHTGREEN);
            levelLine.setStrokeWidth(2);
            treePane.getChildren().add(levelLine);

            // Dibujar número de nivel
            Text levelText = new Text(LEVEL_TEXT_OFFSET_X, y + 5, String.valueOf(i));
            levelText.setFont(new Font(16));
            levelText.setFill(Color.BLACK);
            treePane.getChildren().add(levelText);
        }
    }

    @javafx.fxml.FXML
    public void levelsOnAction(ActionEvent actionEvent) {
        treePane.getChildren().clear();

        if (!tree.isEmpty()) {
            try {
                drawLevels();
                drawBTreeNode(tree.getRoot(), treePane.getPrefWidth() / 2, START_Y, HORIZONTAL_INITIAL_SPACING, 0);
            } catch (TreeException e) {
                txtMessage.setText("Error displaying levels: " + e.getMessage());
            }
        }
        util.FXUtility.showMessage("Information", "The levels have been displayed.");
    }

    @javafx.fxml.FXML
    public void randomizeOnAction(ActionEvent actionEvent) {
        generateRandomTreeValues(); // Generar nuevos valores aleatorios
        addValuesToTree(); // Agregar los valores al árbol actual
        drawTree();
        util.FXUtility.showMessage("Information", "The tree has been randomized.");
    }

    @javafx.fxml.FXML
    public void tourInfoOnAction(ActionEvent actionEvent) {
        String tourInfoContent = "";
        try {
            // Se asume que la interfaz Tree tiene los métodos preOrder(), inOrder(), postOrder()
            tourInfoContent = "--- Traversal Information ---" + "\n" +
                    "Preorden: " + tree.preOrder() + "\n" + "\n" +
                    "Inorden: " + tree.inOrder() + "\n" + "\n" +
                    "Postorden: " + tree.postOrder() + "\n";
        } catch (TreeException e) {
            tourInfoContent = "Error: " + e.getMessage();
        }
        util.FXUtility.showTextAreaAlert("Traversal Information", "Traversal Results", tourInfoContent);
    }

    @javafx.fxml.FXML
    public void handleScrollZoom(ScrollEvent event) {
        double zoomFactor = 1.1;
        if (event.getDeltaY() < 0) {
            zoomFactor = 1 / zoomFactor;
        }
        Pane pane = (Pane) event.getSource();
        pane.setScaleX(pane.getScaleX() * zoomFactor);
        pane.setScaleY(pane.getScaleY() * zoomFactor);
    }

    @javafx.fxml.FXML
    public void AvlOnAction(ActionEvent actionEvent) {
        // Si ya es un AVL, no hacer nada
        if (tree instanceof AVL) {
            util.FXUtility.showMessage("Tree Change", "The tree has been changed to an AVL Tree.");
            return;
        }

        // Cambiar a AVL
        tree = new AVL(); // Crear una nueva instancia de AVL
        addValuesToTree(); // Agregar los valores existentes al nuevo árbol AVL
        drawTree(); // Redibujar el árbol
        util.FXUtility.showMessage("Tree Change", "The tree has been changed to an AVL Tree.");
    }

    @javafx.fxml.FXML
    public void BalanceOnAction(ActionEvent actionEvent) {
        try {
            // Se asume que la interfaz Tree tiene el método isBalanced()
            if (tree.isBalanced()) {
                util.FXUtility.showMessage("Tree Balance", "The current tree is balanced.");
            } else {
                util.FXUtility.showMessage("Tree Balance", "The current tree is NOT balanced.");
            }
        } catch (TreeException e) {
            util.FXUtility.showMessage("Error", "Error checking tree balance: " + e.getMessage());
        }
    }

    @javafx.fxml.FXML
    public void BtsOnAction(ActionEvent actionEvent) {
        // Si ya es un BST, no hacer nada
        if (tree instanceof BST) {
            util.FXUtility.showMessage("Tree Change", "The tree is already of type BST.");
            return;
        }

        // Cambiar a BST
        tree = new BST(); // Crear una nueva instancia de BST
        addValuesToTree(); // Agregar los valores existentes al nuevo árbol BST
        drawTree(); // Redibujar el árbol
        util.FXUtility.showMessage("Tree Change", "The tree has been changed to a Binary Search Tree (BST).");
    }
}