/*
 * IzPack 4
 * http://www.izforge.com/izpack/
 * http://developer.berlios.de/projects/izpack/
 *
 * Copyright (c) 2004 Julien Ponge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.izforge.izpack.util.i18n;

import java.util.Stack;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

/**
 * This class is able to perform some I18N job on XML DOM trees. To do that, it
 * needs a <code>Messages</code> instance that contains the messages needed
 * to perform the localization.
 *
 * For instance, le'ts consider the following XML document part: <code><pre>
 *  &lt;tag param=&quot;%foo.param&quot;&gt;%foo.text&lt;/tag&gt;
 * </pre></code>
 *
 * If your resource bundle contains entries for <code>foo.param</code> and
 * <code>foo.text</code>, then they will be used to perform the keys
 * substitutions. Only text nodes and parameters are processed, the other
 * element types are ignored.
 *
 * @author Julien Ponge <julien@izforge.com>
 */
public class DomTreeLocalizer
{
  /** The messages. */
  protected Messages messages;

  /**
   * Creates a new instance of the DOM localizer for a specific resource
   * bundle.
   *
   * @param messages The messages to use to perform the translations.
   */
  public DomTreeLocalizer(Messages messages)
  {
    this.messages = messages;
  }

  /**
   * Performs the translation on a DOM tree.
   *
   * @param dom The DOM tree to translate.
   */
  public void translate(Document dom)
  {
    Stack stack = new Stack();
    stack.push(dom);

    int i, j;
    int length;
    int alength;
    short type;
    Node cur;
    NodeList nodes;
    Node node;
    NamedNodeMap attrs;
    while (!stack.isEmpty())
    {
      cur = (Node)stack.pop();
      nodes = cur.getChildNodes();
      length = nodes.getLength();
      for (i = 0; i < length; ++i)
      {
        node = nodes.item(i);
        type = node.getNodeType();

        // For the node itself
        switch (type)
        {
          case Node.TEXT_NODE:
            doTranslate(node);
            break;
          case Node.ELEMENT_NODE:
            stack.push(node);
            break;
          default:
            break;
        }

        // For the node attributes
        attrs = node.getAttributes();
        if (attrs == null) continue;
        alength = attrs.getLength();
        for (j = 0; j < alength; ++j)
        {
          doTranslate(attrs.item(j));
        }
      }
    }
  }

  /**
   * Performs the actual translation job.
   *
   * @param Node The node to translate.
   */
  private void doTranslate(Node node)
  {
    String value;
    String translated;

    value = node.getNodeValue();
    if (value.startsWith("%") && value.length() > 1)
    {
      translated = messages.getString(value.substring(1));
      node.setNodeValue(translated);
    }
  }

}
