package org.teamrocket;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jhotdraw.app.Application;
import org.jhotdraw.app.View;
import org.jhotdraw.app.action.AbstractApplicationAction;
import org.jhotdraw.gui.URIChooser;
import org.jhotdraw.util.ResourceBundleUtil;
import org.jhotdraw.util.prefs.PreferencesUtil;
import org.teamrocket.entities.EndStateEntity;
import org.teamrocket.entities.StartStateEntity;
import org.teamrocket.entities.StateEntity;
import org.teamrocket.entities.TransitionEntity;
import org.zachtaylor.jnodalxml.XMLNode;
import org.zachtaylor.jnodalxml.XMLParser;

public class SimulateXMLFileAction extends AbstractApplicationAction {

  public final static String ID = "file.simulatexml";

  /** Creates a new instance. */
  public SimulateXMLFileAction(Application app) {
    super(app);
    ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
    labels.configureAction(this, ID);
  }

  protected URIChooser getChooser(View view) {
    // Note: We pass null here, because we want the application-wide chooser
    return getApplication().getOpenChooser(null);
  }

  @Override
  public void actionPerformed(ActionEvent evt) {
    final Application app = getApplication();
    if (app.isEnabled()) {
      app.setEnabled(false);
      // Search for an empty view
      View emptyView = app.getActiveView();
      if (emptyView == null || !emptyView.isEmpty() || !emptyView.isEnabled()) {
        emptyView = null;
      }

      final View view;
      boolean disposeView;
      if (emptyView == null) {
        view = app.createView();
        app.add(view);
        disposeView = true;
      }
      else {
        view = emptyView;
        disposeView = false;
      }
      URIChooser chooser = getChooser(view);
      chooser.setDialogType(JFileChooser.OPEN_DIALOG);
      if (showDialog(chooser, app.getComponent()) == JFileChooser.APPROVE_OPTION) {
        app.show(view);
        openViewFromURI(view, chooser.getSelectedURI(), chooser);
      }
      else {
        if (disposeView) {
          app.dispose(view);
        }
        app.setEnabled(true);
      }
    }
  }

  protected void openViewFromURI(final View view, final URI uri, final URIChooser chooser) {
    final Application app = getApplication();
    app.setEnabled(true);
    view.setEnabled(false);

    // If there is another view with the same URI we set the multiple open
    // id of our view to max(multiple open id) + 1.
    int multipleOpenId = 1;
    for (View aView : app.views()) {
      if (aView != view && aView.isEmpty()) {
        multipleOpenId = Math.max(multipleOpenId, aView.getMultipleOpenId() + 1);
      }
    }
    view.setMultipleOpenId(multipleOpenId);
    view.setEnabled(false);

    // TODO Open the file

    view.setURI(uri);
    view.setEnabled(true);
    app.setEnabled(true);
    view.getComponent().requestFocus();
    app.addRecentURI(uri);

    try {
      List<XMLNode> data = XMLParser.parse(new File(uri));

      for (XMLNode item : data) {
        if (item.getAttribute("id").equals("startstate")) {
          StartStateEntity ent = new StartStateEntity(null);
          ApplicationModel.addStartStateEntity(ent);
        }
        else if (item.getAttribute("id").equals("endstate")) {
          EndStateEntity ent = new EndStateEntity(null);
          ApplicationModel.addStateEntity(ent);
        }
        else {
          StateEntity ent = new StateEntity(null);
          ent.setLabel(item.getAttribute("id"));

          for (XMLNode eventNode : item.getChildren("event")) {
            for (XMLNode actionNode : eventNode.getChildren("action")) {
              ent.addInternalTransition(eventNode.getAttribute("id"), actionNode.getAttribute("id"));
            }
          }

          ApplicationModel.addStateEntity(ent);
        }
      }

      for (XMLNode item : data) {
        StateEntity ent = findStateEntityByName(item.getAttribute("id"));

        for (XMLNode transitionNode : item.getChildren("transition")) {
          TransitionEntity tent = new TransitionEntity();
          tent.setInput(transitionNode.getAttribute("event"));
          tent.setAction(transitionNode.getAttribute("action"));

          StateEntity pointer = findStateEntityByName(transitionNode.getAttribute("next"));

          tent.setPrev(ent);
          ent.addSuccessor(tent);

          tent.setNext(pointer);
          pointer.addPredecessor(tent);
        }
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    (new Thread(new Simulate())).start();

    // TODO : Close the app
  }

  private StateEntity findStateEntityByName(String name) {
    if (name.equals("startstate"))
      return ApplicationModel.getStartEntity();
    else if (name.equals("endstate")) {
      for (StateEntity ent : ApplicationModel.getStateEntityBucket()) {
        if (ent instanceof EndStateEntity)
          return ent;
      }
      return null;
    }
    else {
      for (StateEntity ent : ApplicationModel.getStateEntityBucket()) {
        if (ent.getName().equals(name))
          return ent;
      }
      return null;
    }
  }

  /**
   * We implement JFileChooser.showDialog by ourselves, so that we can center dialogs properly on screen on Mac OS X.
   */
  public int showDialog(URIChooser chooser, Component parent) {
    final Component finalParent = parent;
    final int[] returnValue = new int[1];
    final JDialog dialog = createDialog(chooser, finalParent);
    dialog.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosing(WindowEvent e) {
        returnValue[0] = JFileChooser.CANCEL_OPTION;
      }
    });
    chooser.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("CancelSelection")) {
          returnValue[0] = JFileChooser.CANCEL_OPTION;
          dialog.setVisible(false);
        }
        else if (e.getActionCommand().equals("ApproveSelection")) {
          returnValue[0] = JFileChooser.APPROVE_OPTION;
          dialog.setVisible(false);
        }
      }
    });
    returnValue[0] = JFileChooser.ERROR_OPTION;
    chooser.rescanCurrentDirectory();

    dialog.setVisible(true);
    // chooser.firePropertyChange("JFileChooserDialogIsClosingProperty", dialog,
    // null);
    dialog.removeAll();
    dialog.dispose();
    return returnValue[0];
  }

  /**
   * We implement JFileChooser.showDialog by ourselves, so that we can center dialogs properly on screen on Mac OS X.
   */
  protected JDialog createDialog(URIChooser chooser, Component parent) throws HeadlessException {
    String title = chooser.getDialogTitle();
    if (chooser instanceof JFileChooser) {
      ((JFileChooser) chooser).getAccessibleContext().setAccessibleDescription(title);
    }

    JDialog dialog;
    Window window = (parent == null || (parent instanceof Window)) ? (Window) parent : SwingUtilities.getWindowAncestor(parent);
    if (window instanceof Frame) {
      dialog = new JDialog((Frame) window, title, true);
    }
    else {
      dialog = new JDialog((Dialog) window, title, true);
    }
    dialog.setComponentOrientation(chooser.getComponent().getComponentOrientation());

    Container contentPane = dialog.getContentPane();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(chooser.getComponent(), BorderLayout.CENTER);

    if (JDialog.isDefaultLookAndFeelDecorated()) {
      boolean supportsWindowDecorations = UIManager.getLookAndFeel().getSupportsWindowDecorations();
      if (supportsWindowDecorations) {
        dialog.getRootPane().setWindowDecorationStyle(JRootPane.FILE_CHOOSER_DIALOG);
      }
    }
    // dialog.pack();
    Preferences prefs = PreferencesUtil.userNodeForPackage(getApplication().getModel().getClass());

    PreferencesUtil.installFramePrefsHandler(prefs, "openChooser", dialog);
    /*
     * if (window.getBounds().isEmpty()) { Rectangle screenBounds = window.getGraphicsConfiguration().getBounds(); dialog.setLocation(screenBounds.x +
     * (screenBounds.width - dialog.getWidth()) / 2, // screenBounds.y + (screenBounds.height - dialog.getHeight()) / 3); } else {
     * dialog.setLocationRelativeTo(parent); }
     */

    return dialog;
  }
}