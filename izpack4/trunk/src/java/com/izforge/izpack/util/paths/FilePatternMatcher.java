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

package com.izforge.izpack.util.paths;

import java.util.regex.Pattern;

/**
 * A class that can be used to match a path against a pattern.
 * The patterns are compatible with those of <a href="http://ant.apache.org/">
 * Ant</a>.
 *
 * @author Julien Ponge <julien@izforge.com>
 */
public class FilePatternMatcher
{
  /** The regexp pattern matcher. */
  protected Pattern patternMatcher;

  /**
   * Constructs a new <code>FilePatternMatcher</code> instance.
   *
   * @param pattern The pattern to match the files against.
   */
  public FilePatternMatcher(String pattern)
  {
    super();

    pattern = pattern.replace('\\', '/');
    pattern = pattern.replaceAll("(\\.)", "(\\\\.)");
    pattern = pattern.replaceAll("(/\\*{2}/)", "/(.*)");
    pattern = pattern.replaceAll("(\\*{2})", "(.*)");
    pattern = pattern.replaceAll("(\\*)(?!\\))", "(\\\\d|\\\\s|\\\\w)*");
    patternMatcher = Pattern.compile(pattern);
  }

  /**
   * Tells wether a given path matches the pattern or not.
   *
   * @return The test result.
   */
  public boolean matches(String path)
  {
    path.replace('\\', '/');
    return patternMatcher.matcher(path).matches();
  }
}
