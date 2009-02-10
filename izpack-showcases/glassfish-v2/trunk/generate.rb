#!/usr/bin/env ruby
#
# Copyright (c) 2007, 2008 Julien Ponge. All rights reserved.
# 
# Permission is hereby granted, free of charge, to any person
# obtaining a copy of this software and associated documentation
# files (the "Software"), to deal in the Software without
# restriction, including without limitation the rights to use,
# copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the
# Software is furnished to do so, subject to the following
# conditions:
# 
# The above copyright notice and this permission notice shall be
# included in all copies or substantial portions of the Software.
# 
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
# EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
# OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
# NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
# HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
# WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
# FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
# OTHER DEALINGS IN THE SOFTWARE.

require 'erb'
require 'open-uri'
require 'fileutils'

# Note: the code is not necessarily clean and always Ruby-ish...

if (ARGV.length != 1)
  puts "Syntax: generate <IzPack home directory>"
  exit(-1)
end

izpack_home = ARGV[0]
platforms   = [:win32, :darwin, :linux, :solaris_x86, :solaris_sparc]
version     = 'v2.1'
build       = 'b60e'

# Download the official binaries
if not File.exist? 'vendor'
  puts "Downloading the binaries..."
  
  name_mapping = {
    'SunOS'     => ['sunos', :solaris_sparc],
    'SunOS_X86' => ['sunos_x86', :solaris_x86],
    'WINNT'     => ['windows', :win32],
    'Linux'     => ['linux', :linux],
    'Darwin'    => ['darwin', :darwin]
  }
  FileUtils.mkdir 'vendor'
  
  name_mapping.each do |variant, target|
    puts "    #{variant}"
    url = "http://java.net/download/javaee5/#{version}_branch/promoted/#{variant}/glassfish-installer-#{version}-#{build}-#{target[0]}.jar"
    puts "    >>> #{url}"
    open(url) do |input|
      output = open("vendor/#{variant}.jar", 'w')
      begin
        loop do
          output.syswrite(input.sysread(4096))
        end
      rescue
        output.close
      end
    end
  end
  
  name_mapping.each do |variant, target|
    system "java -Xmx256m -jar vendor/#{variant}.jar"
    FileUtils.move "glassfish", "#{target[1].to_s}"
  end
end

# Get a list of each platform files
puts "Scanning..."
files = Hash.new
platforms.each do |platform|
  Dir.chdir platform.to_s
  files[platform] = Dir.glob '**/*'
  Dir.chdir '..'
end

# Compute the common and differing parts
puts "Computing the packs sets..."
common_files    = files[:win32]  & files[:darwin] & files[:linux] & files[:solaris_x86] & files[:solaris_sparc]
win32_specific  = files[:win32]  - common_files
darwin_specific = files[:darwin] - common_files
linux_specific  = files[:linux]  - common_files
solaris_x86_specific    = files[:solaris_x86]   - common_files
solaris_sparc_specific  = files[:solaris_sparc] - common_files

# Write the descriptors
puts "Writing the descriptors..."

descriptors = [
  "glassfish-install-#{version}-#{build}.xml",
  "glassfish-install-windows-#{version}-#{build}.xml",
  "glassfish-install-macosx-#{version}-#{build}.xml"
]

tpl = ERB.new(IO.read('template.erb'))
open(descriptors[0], 'w') do |file|
  file.write tpl.result(proc {})
end

common_files.clear
win32_specific.clear
darwin_specific.clear
linux_specific.clear
solaris_x86_specific.clear
solaris_sparc_specific.clear

win32_specific = files[:win32]
open(descriptors[1], 'w') do |file|
  file.write tpl.result(proc {})
end

win32_specific.clear
darwin_specific = files[:darwin]
open(descriptors[2], 'w') do |file|
  file.write tpl.result(proc {})
end

# Compile the installers
puts "Compiling the installers..."

descriptors.each do |descriptor|
  jar = "#{descriptor[0..-5]}.jar"
  system "#{izpack_home}/bin/compile #{descriptor} -h #{izpack_home} -o #{jar} -c deflate -l 9"
end

puts "Done"
