package freemind.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import freemind.main.FreeMind;
import freemind.modes.ModeController;
import freemind.view.MapModule;

public class MenuBar extends JMenuBar {

	private static java.util.logging.Logger logger;
	private static final String MENU_BAR_PREFIX = "menu_bar/";
	private static final String GENERAL_POPUP_PREFIX = "popup/";

	private static final String POPUP_MENU = GENERAL_POPUP_PREFIX + "popup/";

	public static final String INSERT_MENU = MENU_BAR_PREFIX + "insert/";
	private static final String NAVIGATE_MENU = MENU_BAR_PREFIX + "navigate/";
	private static final String VIEW_MENU = MENU_BAR_PREFIX + "view/";
	private static final String HELP_MENU = MENU_BAR_PREFIX + "help/";
	private static final String MINDMAP_MENU = MENU_BAR_PREFIX + "mindmaps/";
	private static final String MENU_MINDMAP_CATEGORY = MINDMAP_MENU + "mindmaps";
	private static final String MODES_MENU = MINDMAP_MENU;
	public static final String EDIT_MENU = MENU_BAR_PREFIX + "edit/";
	private static final String FILE_MENU = MENU_BAR_PREFIX + "file/";
	public static final String FORMAT_MENU = MENU_BAR_PREFIX + "format/";
	private static final String EXTRAS_MENU = MENU_BAR_PREFIX + "extras/";

	private StructuredMenuHolder menuHolder;

	private JPopupMenu mapsPopupMenu;
	Controller c;
	private ActionListener mapsMenuActionListener = new MapsMenuActionListener();

    private JMenu filemenu;
    private JMenu editmenu;
    private JMenu formatmenu;
    private JMenu mapsmenu;

	public MenuBar(Controller controller) {
		this.c = controller;
		if (logger == null) {
			logger = controller.getFrame().getLogger(this.getClass().getName());
		}
	}

	public void updateMenus(ModeController newModeController) {
		this.removeAll();

		menuHolder = new StructuredMenuHolder();

		generateMenuBar();
		generatePopupBar();

		updateFileMenu();
		updateViewMenu();
		updateEditMenu();

		updateMapsMenu(menuHolder, MENU_MINDMAP_CATEGORY + "/");
		updateMapsMenu(menuHolder, POPUP_MENU);
		addAdditionalPopupActions();
		newModeController.updateMenus(menuHolder);

		menuHolder.updateMenus(this, MENU_BAR_PREFIX);
		menuHolder.updateMenus(mapsPopupMenu, GENERAL_POPUP_PREFIX);
    }

	private void generateMenuBar() {
		addFileMenuCategories();
		addEditMenuCategories();
		addViewMenuCategories();
		addInsertMenuCategories();
		addFormatMenuCategories();
		addNavigateMenuCategories();
		addExtrasMenuCategories();
		addMapMenu();
		addModesMenu();
		addHelpPopupMenu();
	}

	private void generatePopupBar() {
		mapsPopupMenu = new FreeMindPopupMenu();
        mapsPopupMenu.setName(c.getResourceString("mindmaps"));
        menuHolder.addCategory(POPUP_MENU + "navigate");
	}

	private void addFileMenuCategories() {
        filemenu = menuHolder.addMenu(new JMenu(c.getResourceString("file")), FILE_MENU + ".");

        menuHolder.addCategory(FILE_MENU + "open");
        menuHolder.addCategory(FILE_MENU + "close");
        menuHolder.addSeparator(FILE_MENU);
        menuHolder.addCategory(FILE_MENU + "export");
        menuHolder.addSeparator(FILE_MENU);
        menuHolder.addCategory(FILE_MENU + "import");
        menuHolder.addSeparator(FILE_MENU);
        menuHolder.addCategory(FILE_MENU + "print");
        menuHolder.addSeparator(FILE_MENU);
        menuHolder.addCategory(FILE_MENU + "last");
        menuHolder.addSeparator(FILE_MENU);
        menuHolder.addCategory(FILE_MENU + "quit");
    }

    private void addEditMenuCategories() {
        editmenu = menuHolder.addMenu(new JMenu(c.getResourceString("edit")), EDIT_MENU + ".");

        menuHolder.addCategory(EDIT_MENU + "undo");
        menuHolder.addSeparator(EDIT_MENU);
        menuHolder.addCategory(EDIT_MENU + "select");
        menuHolder.addSeparator(EDIT_MENU);
        menuHolder.addCategory(EDIT_MENU + "paste");
        menuHolder.addSeparator(EDIT_MENU);
        menuHolder.addCategory(EDIT_MENU + "edit");
        menuHolder.addSeparator(EDIT_MENU);
        menuHolder.addCategory(EDIT_MENU + "find");
    }

    private void addInsertMenuCategories() {
        menuHolder.addMenu(new JMenu(c.getResourceString("menu_insert")), INSERT_MENU + ".");

        menuHolder.addCategory(INSERT_MENU + "nodes");
        menuHolder.addSeparator(INSERT_MENU);
        menuHolder.addCategory(INSERT_MENU + "icons");
        menuHolder.addSeparator(INSERT_MENU);
    }

    private void addViewMenuCategories() {
        menuHolder.addMenu(new JMenu(c.getResourceString("menu_view")), VIEW_MENU + ".");
    }

    private void addFormatMenuCategories() {
        formatmenu = menuHolder.addMenu(new JMenu(c.getResourceString("menu_format")), FORMAT_MENU + ".");
    }

    private void addNavigateMenuCategories() {
        menuHolder.addMenu(new JMenu(c.getResourceString("menu_navigate")), NAVIGATE_MENU + ".");
    }

    private void addExtrasMenuCategories() {
        menuHolder.addMenu(new JMenu(c.getResourceString("menu_extras")), EXTRAS_MENU + ".");
        menuHolder.addCategory(EXTRAS_MENU + "first");
    }

    private void addMapMenu() {
        mapsmenu = menuHolder.addMenu(new JMenu(c.getResourceString("mindmaps")), MINDMAP_MENU + ".");

        menuHolder.addCategory(MINDMAP_MENU + "navigate");
        menuHolder.addSeparator(MINDMAP_MENU);
        menuHolder.addCategory(MENU_MINDMAP_CATEGORY);
        menuHolder.addSeparator(MINDMAP_MENU);
    }

    private void addModesMenu() {
        menuHolder.addCategory(MODES_MENU);
    }

    private void addHelpPopupMenu() {
        menuHolder.addMenu(new JMenu(c.getResourceString("help")), HELP_MENU + ".");
        menuHolder.addAction(c.documentation, HELP_MENU + "doc/documentation");
        menuHolder.addAction(c.freemindUrl, HELP_MENU + "doc/freemind");
        menuHolder.addAction(c.faq, HELP_MENU + "doc/faq");
        menuHolder.addAction(c.keyDocumentation, HELP_MENU + "doc/keyDocumentation");
        menuHolder.addSeparator(HELP_MENU);
        menuHolder.addCategory(HELP_MENU + "bugs");
        menuHolder.addSeparator(HELP_MENU);
        menuHolder.addAction(c.license, HELP_MENU + "about/license");
        menuHolder.addAction(c.about, HELP_MENU + "about/about");
    }

	private void addAdditionalPopupActions() {
		menuHolder.addSeparator(POPUP_MENU);
		JMenuItem newPopupItem;

		if (c.getFrame().isApplet()) {
			newPopupItem = menuHolder.addAction(c.toggleMenubar, POPUP_MENU + "toggleMenubar");
			newPopupItem.setForeground(new Color(100, 80, 80));
		}

		newPopupItem = menuHolder.addAction(c.toggleToolbar, POPUP_MENU + "toggleToolbar");
		newPopupItem.setForeground(new Color(100, 80, 80));

		newPopupItem = menuHolder.addAction(c.toggleLeftToolbar, POPUP_MENU + "toggleLeftToolbar");
		newPopupItem.setForeground(new Color(100, 80, 80));
	}

	private void updateMapsMenu(StructuredMenuHolder holder, String basicKey) {
		MapModuleManager mapModuleManager = c.getMapModuleManager();
		List mapModuleVector = mapModuleManager.getMapModuleVector();
		if (mapModuleVector == null) {
			return;
		}
		ButtonGroup group = new ButtonGroup();
		for (Object aMapModuleVector : mapModuleVector) {
			MapModule mapModule = (MapModule) aMapModuleVector;
			String displayName = mapModule.getDisplayName();
			JRadioButtonMenuItem newItem = new JRadioButtonMenuItem(displayName);
			newItem.setSelected(false);
			group.add(newItem);

			newItem.addActionListener(mapsMenuActionListener);
			newItem.setMnemonic(displayName.charAt(0));

			MapModule currentMapModule = mapModuleManager.getMapModule();
			if (currentMapModule != null) {
				if (mapModule == currentMapModule) {
					newItem.setSelected(true);
				}
			}
			holder.addMenuItem(newItem, basicKey + displayName);
		}
	}

	private void updateFileMenu() {

		menuHolder.addAction(c.page, FILE_MENU + "print/pageSetup");
		JMenuItem print = menuHolder.addAction(c.print, FILE_MENU + "print/print");
		print.setAccelerator(KeyStroke.getKeyStroke(c.getFrame().getAdjustableProperty("keystroke_print")));

		JMenuItem printPreview = menuHolder.addAction(c.printPreview, FILE_MENU + "print/printPreview");
		printPreview.setAccelerator(KeyStroke.getKeyStroke(c.getFrame().getAdjustableProperty("keystroke_print_preview")));

		JMenuItem close = menuHolder.addAction(c.close, FILE_MENU + "close/close");
		close.setAccelerator(KeyStroke.getKeyStroke(c.getFrame().getAdjustableProperty("keystroke_close")));

		JMenuItem quit = menuHolder.addAction(c.quit, FILE_MENU + "quit/quit");
		quit.setAccelerator(KeyStroke.getKeyStroke(c.getFrame().getAdjustableProperty("keystroke_quit")));
	}

	private void updateEditMenu() {
		JMenuItem moveToRoot = menuHolder.addAction(c.moveToRoot, NAVIGATE_MENU + "nodes/moveToRoot");
		moveToRoot.setAccelerator(KeyStroke.getKeyStroke(c.getFrame().getAdjustableProperty("keystroke_moveToRoot")));

		JMenuItem previousMap = menuHolder.addAction(c.navigationPreviousMap, MINDMAP_MENU + "navigate/navigationPreviousMap");
		previousMap.setAccelerator(KeyStroke.getKeyStroke(c.getFrame().getAdjustableProperty(FreeMind.KEYSTROKE_PREVIOUS_MAP)));

		JMenuItem nextMap = menuHolder.addAction(c.navigationNextMap, MINDMAP_MENU + "navigate/navigationNextMap");
		nextMap.setAccelerator(KeyStroke.getKeyStroke(c.getFrame().getAdjustableProperty(FreeMind.KEYSTROKE_NEXT_MAP)));

		JMenuItem MoveMapLeft = menuHolder.addAction(c.navigationMoveMapLeftAction, MINDMAP_MENU + "navigate/navigationMoveMapLeft");
		MoveMapLeft.setAccelerator(KeyStroke.getKeyStroke(c.getFrame().getAdjustableProperty(FreeMind.KEYSTROKE_MOVE_MAP_LEFT)));

		JMenuItem MoveMapRight = menuHolder.addAction(c.navigationMoveMapRightAction, MINDMAP_MENU + "navigate/navigationMoveMapRight");
		MoveMapRight.setAccelerator(KeyStroke.getKeyStroke(c.getFrame().getAdjustableProperty(FreeMind.KEYSTROKE_MOVE_MAP_RIGHT)));
	}

	private void updateViewMenu() {
		JMenuItem toggleToolbar = menuHolder.addAction(c.toggleToolbar, VIEW_MENU + "toolbars/toggleToolbar");
		JMenuItem toggleLeftToolbar = menuHolder.addAction(c.toggleLeftToolbar, VIEW_MENU + "toolbars/toggleLeftToolbar");

		menuHolder.addSeparator(VIEW_MENU);

		JMenuItem showSelectionAsRectangle = menuHolder.addAction(c.showSelectionAsRectangle, VIEW_MENU + "general/selectionAsRectangle");

		JMenuItem zoomIn = menuHolder.addAction(c.zoomIn, VIEW_MENU + "zoom/zoomIn");
		zoomIn.setAccelerator(KeyStroke.getKeyStroke(c.getFrame().getAdjustableProperty("keystroke_zoom_in")));

		JMenuItem zoomOut = menuHolder.addAction(c.zoomOut, VIEW_MENU + "zoom/zoomOut");
		zoomOut.setAccelerator(KeyStroke.getKeyStroke(c.getFrame().getAdjustableProperty("keystroke_zoom_out")));

		menuHolder.addSeparator(VIEW_MENU);
	}

	private void addOptionSet(Action action, String[] textIDs, JMenu menu, String selectedTextID) {
		ButtonGroup group = new ButtonGroup();
		for (String textID : textIDs) {
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(action);
			item.setText(c.getResourceString(textID));
			item.setActionCommand(textID);
			group.add(item);
			menu.add(item);
			if (selectedTextID != null) {
				item.setSelected(selectedTextID.equals(textID));
			}
			String keystroke = c.getFrame().getAdjustableProperty("keystroke_" + textID);
			if (keystroke != null) {
				item.setAccelerator(KeyStroke.getKeyStroke(keystroke));
			}
		}
	}

	JPopupMenu getMapsPopupMenu() { // visible only in controller package
		return mapsPopupMenu;
	}

	private class MapsMenuActionListener implements ActionListener {
		public void actionPerformed(final ActionEvent e) {
			SwingUtilities.invokeLater(() -> c.getMapModuleManager().changeToMapModule(
                    e.getActionCommand()));
		}
	}

	private class ModesMenuActionListener implements ActionListener {
		public void actionPerformed(final ActionEvent e) {
			SwingUtilities.invokeLater(() -> c.createNewMode(e.getActionCommand()));
		}
	}

	public boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
		return super.processKeyBinding(ks, e, condition, pressed);
	}
}
