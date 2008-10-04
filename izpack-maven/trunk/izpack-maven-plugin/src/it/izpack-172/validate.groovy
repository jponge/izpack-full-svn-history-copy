assert new File(basedir, 'target/izpack/install.xml').exists();


content = new File(basedir,'target/izpack/install.xml').text;

assert ! list.contains( '@{app.version.static}' )
assert list.contains( '1.5.6' )
