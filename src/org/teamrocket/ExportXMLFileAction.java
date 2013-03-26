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
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.gui.JFileURIChooser;
import org.jhotdraw.gui.JSheet;
import org.jhotdraw.gui.URIChooser;
import org.jhotdraw.gui.event.SheetEvent;
import org.jhotdraw.gui.event.SheetListener;
import org.jhotdraw.util.ResourceBundleUtil;

import com.zachtaylor.jnodalxml.XMLNode;

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

    //  DefaultDraw
      List<Figure> figures = d.getChildren();

      // TODO : Write XML to the file
      List<XMLNode> nodes = new ArrayList<XMLNode>();
      if (figures.isEmpty())
      	writer.write("\n");
      
      for (Figure f : figures) {
      	XMLNode n = new XMLNode("State");
      	if (!(f instanceof StateFigure))
      		continue;
      	
      	n.setAttribute("name", ((StateFigure) f).getName());
      	n.setAttribute("description", ((StateFigure) f).getDescription());
      	
      	// list of each figure's children
      	List<Figure> chef = ((StateFigure) f).getChildren();
      	
      	for (Figure child : chef) {
      		if (!(child instanceof TransitionFigure))
      			continue;
      		
      		XMLNode childNode = new XMLNode("transition");
      		childNode.setAttribute("trigger", ((TransitionFigure) child).getData().getInput());
      		childNode.setAttribute("action", ((TransitionFigure) child).getData().getAction());
      		childNode.setAttribute("next", ((TransitionFigure) child).getData().getNext().getName());
      		
      		n.addChild(childNode);
      	}
      	
      	nodes.add(n);
      }
      // debugging printing
      for (XMLNode q : nodes) {
      	System.out.println(q.toString());
      }
      
      
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
