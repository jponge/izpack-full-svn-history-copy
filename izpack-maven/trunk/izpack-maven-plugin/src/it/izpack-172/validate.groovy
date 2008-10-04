assert new File(basedir, 'target/izpack/install.xml').exists();


content = new File(basedir,'target/izpack/install.xml').text;

assert ! content.contains( '@{app.version.static}' )
assert content.contains( '1.5.6' )
