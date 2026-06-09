package com.restaurant;

import com.restaurant.command.*;
import com.restaurant.decorator.*;
import com.restaurant.domain.*;
import com.restaurant.factory.MenuItemFactory;
import com.restaurant.observer.*;
import com.restaurant.strategy.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class MainGUI extends JFrame {

    // ── Core backend objects ──────────────────────────────────────────────────
    private final OrderQueue    queue   = OrderQueue.getInstance();
    private final CommandHistory history = new CommandHistory();

    // ── Current order being built ─────────────────────────────────────────────
    private Order currentOrder;

    // ── UI panels ─────────────────────────────────────────────────────────────
    private JTextArea kitchenLog;
    private JTextArea orderLog;
    private JLabel    statusLabel;
    private JComboBox<String> customerNameBox;
    private JComboBox<String> pricingBox;
    private JCheckBox cheeseBox, baconBox, sauceBox;
    private JList<String> orderItemsList;
    private DefaultListModel<String> orderItemsModel;

    // ── Colours ───────────────────────────────────────────────────────────────
    private static final Color BG        = new Color(18, 18, 24);
    private static final Color PANEL_BG  = new Color(28, 30, 40);
    private static final Color ACCENT    = new Color(0, 200, 140);
    private static final Color TEXT      = new Color(220, 220, 230);
    private static final Color MUTED     = new Color(130, 130, 150);
    private static final Color BORDER_C  = new Color(50, 54, 70);
    private static final Color BTN_BG    = new Color(40, 44, 60);
    private static final Color DANGER    = new Color(220, 80, 80);

    public MainGUI() {
        super("Restaurant Ordering System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 680);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);

        // Register observers — they write to kitchenLog and orderLog
        setupObservers();

        // Build UI
        setLayout(new BorderLayout(10, 10));
        add(buildTopBar(),    BorderLayout.NORTH);
        add(buildMainArea(),  BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);

        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setVisible(true);
    }

    // ── Observers ─────────────────────────────────────────────────────────────

    private void setupObservers() {
        queue.addObserver((order, event) -> {
            String msg = switch (event) {
                case "PLACED"    -> "[KITCHEN] New order #" + order.getId() + " for " + order.getCustomerName();
                case "PREPARING" -> "[KITCHEN] Order #" + order.getId() + " is being prepared...";
                case "READY"     -> "[KITCHEN] Order #" + order.getId() + " is READY!";
                case "DELIVERED" -> "[KITCHEN] Order #" + order.getId() + " delivered.";
                case "CANCELLED" -> "[KITCHEN] Order #" + order.getId() + " CANCELLED.";
                default          -> "";
            };
            appendToLog(kitchenLog, msg);
        });

        queue.addObserver((order, event) -> {
            String msg = switch (event) {
                case "PLACED"    -> "Hi " + order.getCustomerName() + "! Order #" + order.getId() + " received.";
                case "READY"     -> "Order #" + order.getId() + " is ready to collect!";
                case "DELIVERED" -> "Enjoy your meal, " + order.getCustomerName() + "! Total: $" + String.format("%.2f", order.getTotal());
                case "CANCELLED" -> "Sorry " + order.getCustomerName() + ", order #" + order.getId() + " was cancelled.";
                default          -> "";
            };
            appendToLog(orderLog, msg);
        });
    }

    // ── Top bar ───────────────────────────────────────────────────────────────

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 8));
        bar.setBackground(PANEL_BG);
        bar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_C));

        JLabel title = new JLabel("◈  Restaurant Ordering System");
        title.setForeground(ACCENT);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        bar.add(title);

        bar.add(makeSeparator());

        bar.add(muted("Customer:"));

        customerNameBox = new JComboBox<>(
            new String[]{"Alice", "Bob", "Carol", "Dave"}
);

        styleCombo(customerNameBox);
        bar.add(customerNameBox);

// Add Customer button
        JButton addCustomerBtn = new JButton("+");
        addCustomerBtn.addActionListener(e -> addCustomer());
        bar.add(addCustomerBtn);

        bar.add(muted("Pricing:"));
        pricingBox = new JComboBox<>(new String[]{"Regular", "Happy Hour (20% off)", "Loyalty (15% off)"});
        styleCombo(pricingBox);
        bar.add(pricingBox);

        return bar;
    }

    // ── Main area ─────────────────────────────────────────────────────────────

    private JPanel buildMainArea() {
        JPanel main = new JPanel(new GridLayout(1, 3, 10, 0));
        main.setBackground(BG);
        main.add(buildMenuPanel());
        main.add(buildOrderPanel());
        main.add(buildLogsPanel());
        return main;
    }

    private JPanel buildMenuPanel() {
        JPanel panel = styledPanel("Menu");

        // Item buttons
        String[][] items = {
            {"burger",         "Classic Burger   $8.99"},
            {"burger-deluxe",  "Deluxe Burger    $11.99"},
            {"pizza",          "Margherita Pizza $10.99"},
            {"pizza-pepperoni","Pepperoni Pizza  $12.99"},
            {"pizza-veggie",   "Veggie Pizza     $11.49"},
            {"drink-soda",     "Soda             $2.49"},
            {"drink-juice",    "Fresh Juice      $3.49"},
            {"drink-water",    "Still Water      $1.49"},
        };

        panel.add(sectionLabel("Select item"));
        for (String[] item : items) {
            JButton btn = menuButton(item[1]);
            btn.addActionListener(e -> addItemToOrder(item[0]));
            panel.add(btn);
        }

        // Toppings
        panel.add(Box.createVerticalStrut(12));
        panel.add(sectionLabel("Toppings"));
        cheeseBox = styledCheckbox("Extra Cheese  +$0.75");
        baconBox  = styledCheckbox("Bacon         +$1.25");
        sauceBox  = styledCheckbox("Extra Sauce   +$0.50");
        panel.add(cheeseBox);
        panel.add(baconBox);
        panel.add(sauceBox);

        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel buildOrderPanel() {
        JPanel panel = styledPanel("Current Order");

        orderItemsModel = new DefaultListModel<>();
        orderItemsList  = new JList<>(orderItemsModel);
        orderItemsList.setBackground(new Color(22, 24, 34));
        orderItemsList.setForeground(TEXT);
        orderItemsList.setFont(new Font("Monospaced", Font.PLAIN, 12));
        orderItemsList.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        JScrollPane scroll = new JScrollPane(orderItemsList);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_C));
        scroll.setBackground(BG);
        panel.add(scroll);

        statusLabel = new JLabel("No active order");
        statusLabel.setForeground(MUTED);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
        panel.add(statusLabel);

        // Action buttons
        panel.add(Box.createVerticalStrut(10));
        panel.add(sectionLabel("Actions"));

        JButton newOrderBtn  = actionButton("New Order",    ACCENT);
        JButton placeBtn     = actionButton("Place Order",  new Color(60, 180, 100));
        JButton advanceBtn   = actionButton("Advance",      new Color(80, 140, 220));
        JButton cancelBtn    = actionButton("Cancel Order", DANGER);
        JButton undoBtn      = actionButton("Undo",         new Color(160, 100, 220));

        newOrderBtn.addActionListener(e -> newOrder());
        placeBtn.addActionListener(e -> placeOrder());
        advanceBtn.addActionListener(e -> advanceOrder());
        cancelBtn.addActionListener(e -> cancelOrder());
        undoBtn.addActionListener(e -> undo());

        JPanel btnGrid = new JPanel(new GridLayout(3, 2, 6, 6));
        btnGrid.setBackground(PANEL_BG);
        btnGrid.add(newOrderBtn);
        btnGrid.add(placeBtn);
        btnGrid.add(advanceBtn);
        btnGrid.add(cancelBtn);
        btnGrid.add(undoBtn);
        btnGrid.add(new JLabel()); // spacer

        panel.add(btnGrid);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel buildLogsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG);

        // Kitchen log
        JPanel kitchenPanel = styledPanel("Kitchen Display");
        kitchenLog = styledTextArea();
        kitchenPanel.add(new JScrollPane(kitchenLog));
        kitchenPanel.setPreferredSize(new Dimension(0, 280));

        // Customer log
        JPanel customerPanel = styledPanel("Customer Notifications");
        orderLog = styledTextArea();
        customerPanel.add(new JScrollPane(orderLog));

        panel.add(kitchenPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(customerPanel);
        return panel;
    }

    private JPanel buildBottomBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bar.setBackground(PANEL_BG);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_C));
        JLabel hint = new JLabel("  Tip: Create a new order first, add items, then Place Order.");
        hint.setForeground(MUTED);
        hint.setFont(new Font("SansSerif", Font.PLAIN, 12));
        bar.add(hint);
        return bar;
    }

    // ── Button actions ────────────────────────────────────────────────────────

    private void newOrder() {
        String name = (String) customerNameBox.getSelectedItem();
        currentOrder = new Order(name);

        // Apply selected pricing strategy
        switch (pricingBox.getSelectedIndex()) {
            case 1 -> currentOrder.setPricingStrategy(new HappyHourPricing());
            case 2 -> currentOrder.setPricingStrategy(new LoyaltyPricing());
            default -> currentOrder.setPricingStrategy(new RegularPricing());
        }

        orderItemsModel.clear();
        updateStatus();
        appendToLog(kitchenLog, "--- New order started for " + name + " ---");
    }

    private void addCustomer() {
    String name = JOptionPane.showInputDialog(
        this,
        "Enter customer name:"
    );

    if (name != null && !name.trim().isEmpty()) {
        customerNameBox.addItem(name.trim());
        customerNameBox.setSelectedItem(name.trim());
    }
}

    private void addItemToOrder(String type) {
        if (currentOrder == null) {
            showError("Create a new order first!");
            return;
        }
        if (currentOrder.getStatus() != null &&
            currentOrder.getStatus() != OrderStatus.PLACED) {
            showError("Cannot modify an order after it has been placed.");
            return;
}
        com.restaurant.domain.MenuItem item = MenuItemFactory.create(type);

        // Apply checked decorators
        if (cheeseBox.isSelected()) item = new CheeseDecorator(item);
        if (baconBox.isSelected())  item = new BaconDecorator(item);
        if (sauceBox.isSelected())  item = new SauceDecorator(item);

        currentOrder.addItem(item);
        orderItemsModel.addElement(String.format("%-32s $%.2f", item.getDescription(), item.getPrice()));
        updateStatus();

        // Reset checkboxes
        cheeseBox.setSelected(false);
        baconBox.setSelected(false);
        sauceBox.setSelected(false);
    }

    private void placeOrder() {
        if (currentOrder == null || currentOrder.getItems().isEmpty()) {
            showError("Add items to your order first!");
            return;
        }
        history.execute(new PlaceOrderCommand(currentOrder, queue));
        updateStatus();
    }

    private void advanceOrder() {
        if (currentOrder == null) {
            showError("No active order!");
            return;
    }

    if (currentOrder.getStatus() == null) {
        showError("Place the order first!");
        return;
    }

    history.execute(new AdvanceOrderCommand(currentOrder, queue));
    updateStatus();
}

    private void cancelOrder() {
        if (currentOrder == null) {
            showError("No active order!");
            return;
    }

    history.execute(new CancelOrderCommand(currentOrder, queue));
    currentOrder = null;
    orderItemsModel.clear();
    updateStatus();
}

    private void undo() {
        history.undo();
        updateStatus();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void updateStatus() {
        if (currentOrder == null) {
            statusLabel.setText("No active order");
            statusLabel.setForeground(MUTED);
        } else {
            double total = currentOrder.getTotal();
            statusLabel.setText(String.format(
        "<html><b>Order #%d</b> | %s | Total: $%.2f | %s</html>",
                currentOrder.getId(),
                currentOrder.getCustomerName(),
                total,
                currentOrder.getStatus() == null ? "Building..." : currentOrder.getStatus()
));
            statusLabel.setForeground(ACCENT);
        }
    }

    private void appendToLog(JTextArea area, String msg) {
        if (msg == null || msg.isEmpty()) return;
        SwingUtilities.invokeLater(() -> {
            area.append(msg + "\n");
            area.setCaretPosition(area.getDocument().getLength());
        });
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Oops", JOptionPane.WARNING_MESSAGE);
    }

    // ── Style helpers ─────────────────────────────────────────────────────────

    private JPanel styledPanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PANEL_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JLabel lbl = new JLabel(title);
        lbl.setForeground(ACCENT);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        panel.add(lbl);
        return panel;
    }

    private JTextArea styledTextArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setBackground(new Color(14, 16, 22));
        area.setForeground(new Color(180, 220, 190));
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }

    private JButton menuButton(String text) {
        JButton btn = new JButton(text);

        btn.setBackground(BTN_BG);
        btn.setForeground(TEXT);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Monospaced", Font.PLAIN, 12));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(55, 60, 80)); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(BTN_BG); }
        });
        return btn;
    }

    private JButton actionButton(String text, Color accent) {
        JButton btn = new JButton(text);
        btn.setBackground(BTN_BG);
        btn.setForeground(accent);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accent.darker()),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JCheckBox styledCheckbox(String text) {
        JCheckBox box = new JCheckBox(text);
        box.setBackground(PANEL_BG);
        box.setForeground(TEXT);
        box.setFont(new Font("Monospaced", Font.PLAIN, 12));
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        return box;
    }

    private void styleCombo(JComboBox<String> combo) {
        combo.setBackground(BTN_BG);
        combo.setForeground(TEXT);
        combo.setFont(new Font("SansSerif", Font.PLAIN, 12));
    }

    private JLabel muted(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(MUTED);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return lbl;
    }

    private JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel(text.toUpperCase());
        lbl.setForeground(MUTED);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        return lbl;
    }

    private JSeparator makeSeparator() {
        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setPreferredSize(new Dimension(1, 20));
        sep.setForeground(BORDER_C);
        return sep;
    }

    // ── Entry point ───────────────────────────────────────────────────────────

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new MainGUI();
        });
    }
}