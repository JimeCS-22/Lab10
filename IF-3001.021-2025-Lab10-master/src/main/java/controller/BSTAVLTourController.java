package controller;

import domain.*;
import javafx.event.ActionEvent;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BSTAVLTourController {
    @javafx.fxml.FXML
    private Text txtMessage;
    @javafx.fxml.FXML
    private Pane mainPain;
    @javafx.fxml.FXML
    private Button inOrderButton;
    @javafx.fxml.FXML
    private RadioButton Bst;
    @javafx.fxml.FXML
    private Button preOrderButton;
    @javafx.fxml.FXML
    private RadioButton Avl;
    @javafx.fxml.FXML
    private Pane buttonPane;
    @javafx.fxml.FXML
    private Button autoButton1;
    @javafx.fxml.FXML
    private Button postOrderButton;
    @javafx.fxml.FXML
    private Pane treePane;
    @javafx.fxml.FXML
    private AnchorPane AP;
    @javafx.fxml.FXML
    private Text ordertxtMessage;

    private Tree tree;
    private int[] treeValues;
    private Map<BTreeNode, Text> positionLabels = new HashMap<>();
    private Map<BTreeNode, Circle> nodeCircles = new HashMap<>();

    // Constantes para el dibujo del árbol
    private static final double NODE_RADIUS = 15;
    private static final double VERTICAL_SPACING = 90;
    private static final double HORIZONTAL_INITIAL_SPACING = 300;
    private static final double HORIZONTAL_REDUCTION_FACTOR = 0.5;
    private static final double START_Y = 50;

    @javafx.fxml.FXML
    public void initialize() {
        ToggleGroup treeTypeGroup = new ToggleGroup();
        Bst.setToggleGroup(treeTypeGroup);
        Avl.setToggleGroup(treeTypeGroup);

        tree = new BST(); // BST por defecto
        Bst.setSelected(true);

        generateRandomTreeValues(); // Generará exactamente 30 valores
        addValuesToTree();
        drawTree();
    }

    private void generateRandomTreeValues() {
        treeValues = new int[30]; // Array fijo de 30 elementos
        for (int i = 0; i < 30; i++) {
            treeValues[i] = util.Utility.random(51); // Valores entre 0 y 50
        }
    }

    private void addValuesToTree() {
        tree.clear();
        for (int value : treeValues) {
            tree.add(value);
        }
    }

    private void drawTree() {
        treePane.getChildren().clear();
        positionLabels.clear();
        nodeCircles.clear();

        if (!tree.isEmpty()) {
            drawBTreeNode((BTreeNode) tree.getRoot(),
                    treePane.getPrefWidth() / 2,
                    START_Y,
                    HORIZONTAL_INITIAL_SPACING,
                    0);
        }
    }

    private void drawBTreeNode(BTreeNode node, double x, double y, double hSpace, int level) {
        if (node == null) return;

        // Dibujar nodo (círculo)
        Circle circle = new Circle(x, y, NODE_RADIUS, Color.LIGHTBLUE);
        circle.setStroke(Color.BLACK);

        // Texto del valor del nodo (centrado)
        Text valueText = new Text(x - 5, y + 5, String.valueOf(node.data));
        valueText.setFont(new Font(12));

        // Texto del índice DEBAJO del nodo
        Text positionText = new Text(x - 5,  y + NODE_RADIUS + 15, "");
        positionText.setFill(Color.RED);
        positionText.setFont(new Font(10));

        treePane.getChildren().addAll(circle, valueText, positionText);
        nodeCircles.put(node, circle);
        positionLabels.put(node, positionText);

        // Dibujar hijos
        double childY = y + VERTICAL_SPACING;
        double childHSpace = hSpace * HORIZONTAL_REDUCTION_FACTOR;

        // Hijo izquierdo
        if (node.left != null) {
            double leftX = x - hSpace / 2;
            drawConnection(x, y, leftX, childY);
            drawBTreeNode(node.left, leftX, childY, childHSpace, level + 1);
        }

        // Hijo derecho
        if (node.right != null) {
            double rightX = x + hSpace / 2;
            drawConnection(x, y, rightX, childY);
            drawBTreeNode(node.right, rightX, childY, childHSpace, level + 1);
        }
    }

    private void drawConnection(double startX, double startY, double endX, double endY) {
        Line line = new Line(startX, startY + NODE_RADIUS, endX, endY - NODE_RADIUS);
        treePane.getChildren().add(line);
    }

    private void updatePositions(String traversalType) {
        // Limpiar posiciones anteriores
        positionLabels.values().forEach(text -> text.setText(""));

        // Configurar texto según el tipo de recorrido
        String orderMessage = switch (traversalType) {
            case "preOrder" -> "Pre Order Transversal Tour (N-L-R)";
            case "inOrder" -> "In Order Transversal Tour (L-N-R)";
            case "postOrder" -> "Post Order Transversal Tour (L-R-N)";
            default -> "";
        };
        ordertxtMessage.setText(orderMessage);

        // Obtener nodos en el orden especificado
        List<BTreeNode> traversalNodes = getTraversalNodes(traversalType, (BTreeNode) tree.getRoot());

        // Actualizar etiquetas de posición
        for (int i = 0; i < traversalNodes.size(); i++) {
            BTreeNode node = traversalNodes.get(i);
            Text positionText = positionLabels.get(node);
            if (positionText != null) {
                positionText.setText(String.valueOf(i + 1));
            }
        }
    }

    private List<BTreeNode> getTraversalNodes(String traversalType, BTreeNode root) {
        List<BTreeNode> result = new ArrayList<>();
        switch (traversalType) {
            case "preOrder" -> preOrder(root, result);
            case "inOrder" -> inOrder(root, result);
            case "postOrder" -> postOrder(root, result);
        }
        return result;
    }

    private void preOrder(BTreeNode node, List<BTreeNode> list) {
        if (node != null) {
            list.add(node);
            preOrder(node.left, list);
            preOrder(node.right, list);
        }
    }

    private void inOrder(BTreeNode node, List<BTreeNode> list) {
        if (node != null) {
            inOrder(node.left, list);
            list.add(node);
            inOrder(node.right, list);
        }
    }

    private void postOrder(BTreeNode node, List<BTreeNode> list) {
        if (node != null) {
            postOrder(node.left, list);
            postOrder(node.right, list);
            list.add(node);
        }
    }

    // Métodos de los botones
    @javafx.fxml.FXML
    public void preOrderOnAction(ActionEvent actionEvent) {
        updatePositions("preOrder");
    }

    @javafx.fxml.FXML
    public void inOrderOnAction(ActionEvent actionEvent) {
        updatePositions("inOrder");
    }

    @javafx.fxml.FXML
    public void postOrderOnAction(ActionEvent actionEvent) {
        updatePositions("postOrder");
    }

    @javafx.fxml.FXML
    public void BtsOnAction(ActionEvent actionEvent) {
        if (!(tree instanceof BST)) {
            tree = new BST();
            addValuesToTree();
            drawTree();
            util.FXUtility.showMessage("Tree Change", "Changed to Binary Search Tree (BST)");
        }
    }

    @javafx.fxml.FXML
    public void AvlOnAction(ActionEvent actionEvent) {
        if (!(tree instanceof AVL)) {
            tree = new AVL();
            addValuesToTree();
            drawTree();
            util.FXUtility.showMessage("Tree Change", "Changed to AVL Tree");
        }
    }

    @javafx.fxml.FXML
    public void randomizeOnAction(ActionEvent actionEvent) {
        generateRandomTreeValues();
        addValuesToTree();
        drawTree();
        util.FXUtility.showMessage("Random Tree", "Generated new random tree");
    }

    @javafx.fxml.FXML
    private void handleScrollZoom(ScrollEvent event) {
        double zoomFactor = 1.1;
        if (event.getDeltaY() < 0) {
            zoomFactor = 1 / zoomFactor;
        }
        Pane pane = (Pane) event.getSource();
        pane.setScaleX(pane.getScaleX() * zoomFactor);
        pane.setScaleY(pane.getScaleY() * zoomFactor);
    }
}
