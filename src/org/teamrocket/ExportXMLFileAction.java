package org.teamrocket;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import org.jhotdraw.app.Application;
import org.jhotdraw.app.View;
import org.jhotdraw.app.action.AbstractViewAction;
import org.jhotdraw.gui.JFileURIChooser;
import org.jhotdraw.gui.JSheet;
import org.jhotdraw.gui.URIChooser;
import org.jhotdraw.gui.event.SheetEvent;
import org.jhotdraw.gui.event.SheetListener;
import org.jhotdraw.util.ResourceBundleUtil;
import org.teamrocket.entities.StateEntity;
import org.zachtaylor.jnodalxml.XMLNode;

import edu.umd.cs.findbugs.annotations.Nullable;

public class ExportXMLFileAction extends AbstractViewAction {

  public final static String ID = "file.exportxml";
  private Component oldFocusOwner;

  /** Creates a new instance. */
  public ExportXMLFileAction(Application app, @Nullable View view) {
    super(app, view);
    ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
    labels.configureAction(this, ID);
  }

  @Override
  public void actionPerformed(ActionEvent evt) {
    final View view = (View) getActiveView();
    if (view.isEnabled()) {
      ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");

      oldFocusOwner = SwingUtilities.getWindowAncestor(view.getComponent()).getFocusOwner();
      view.setEnabled(false);

      URIChooser fileChooser = getApplication().getExportChooser(view);

      JSheet.showSheet(fileChooser, view.getComponent(), labels.getString("filechooser.export"), new SheetListener() {

        @Override
        public void optionSelected(final SheetEvent evt) {
          if (evt.getOption() == JFileChooser.APPROVE_OPTION) {
            final URI uri = evt.getChooser().getSelectedURI();
            if (evt.getChooser() instanceof JFileURIChooser) {
              exportView(view, uri, evt.getChooser());
            }
            else {
              exportView(view, uri, null);
            }
          }
          else {
            view.setEnabled(true);
            if (oldFocusOwner != null) {
              oldFocusOwner.requestFocus();
            }
          }
        }
      });
    }
  }

  protected void exportView(final View view, final URI uri, @Nullable final URIChooser chooser) {
    try {
      File file = new File(uri);

      if (!file.exists())
        file.createNewFile();

      BufferedWriter writer = new BufferedWriter(new FileWriter(file));

      List<StateEntity> figures = ApplicationModel.getStateEntity();

      List<XMLNode> nodes = new ArrayList<XMLNode>();
      if (figures.isEmpty())
        writer.write("\n");

      for (StateEntity e : figures) {
        nodes.add(e.toXML());
      }
      // TODO : Add transitions to save file
      // TODO : Write XML to the file

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}