/* create database and table */
create database if not exists maple; 
use maple;
drop table if exists patterns;
create table patterns(id int auto_increment primary key, class varchar(256), method varchar(256), pattern varchar(256), support int, isRequired BOOLEAN, description varchar(256), votes int default 0, downvotes int default 0,links TEXT default NULL); 

insert into patterns (id, class, method, pattern, support, isRequired, description) values (0, 'SwingUtilities', 'invokeLater', 'new Runnable()@true, invokeLater(Runnable)@true', 20406, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (1, 'JFrame', 'pack', 'pack()@true, setVisible(boolean)@true', 6651, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (2, 'JFrame', 'pack', 'new JFrame(String)@true, pack()@true', 4550, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (3, 'SharedPreferences', 'getString', 'getString(String,String)@true, IF, END_BLOCK', 5013, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (4, 'MessageDigest', 'digest', 'TRY, digest()@true, END_BLOCK, CATCH(Exception), END_BLOCK', 8814, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (5, 'MessageDigest', 'digest', 'getInstance(String)@true, digest()@true', 7048, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (6, 'FileChannel', 'write', 'TRY, write(ByteBuffer)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 1829, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (7, 'FileChannel', 'write', 'TRY, write(ByteBuffer)@true, END_BLOCK, CATCH(IOException), END_BLOCK', 1495, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (8, 'FileChannel', 'write', 'TRY, getChannel()@true, write(ByteBuffer)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 1369, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (9, 'FileChannel', 'write', 'getChannel()@true, write(ByteBuffer)@true', 1359, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (10, 'FileChannel', 'write', 'TRY, write(ByteBuffer)@true, close()@true, END_BLOCK, CATCH(Exception), END_BLOCK', 1267, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (11, 'FileChannel', 'write', 'write(ByteBuffer)@true, close()@true', 1262, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (12, 'SimpleDateFormat', 'SimpleDateFormat', 'new SimpleDateFormat(String)@true, format(Date)@true', 23490, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (13, 'SimpleDateFormat', 'SimpleDateFormat', 'TRY, new SimpleDateFormat(String)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 17961, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (14, 'SimpleDateFormat', 'SimpleDateFormat', 'new SimpleDateFormat(String)@true, parse(String)@true', 9783, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (15, 'Pattern', 'compile', 'compile(String)@true, matcher(String)@true', 24889, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (16, 'Activity', 'setContentView', 'onCreate(Bundle)@true, setContentView(View)@true', 96848, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (17, 'URLEncoder', 'encode', 'TRY, encode(String,String)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 14113, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (18, 'Intent', 'putExtra', 'new Intent(String,Uri)@true, putExtra(String,String)@true', 15583, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (19, 'Intent', 'putExtra', 'new Intent(String)@true, putExtra(String,String)@true', 12467, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (20, 'StringBuilder', 'append', 'new StringBuilder()@true, append(String)@true', 237169, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (21, 'Bundle', 'getString', 'IF, getString(String)@rcv!=null, END_BLOCK', 4714, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (22, 'Iterator', 'next', 'LOOP, next()@rcv.hasNext(), END_BLOCK', 253734, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (23, 'Iterator', 'next', 'next()@rcv.hasNext()', 253385, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (24, 'Iterator', 'next', 'iterator()@true, next()@rcv.hasNext()', 219572, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (25, 'Iterator', 'next', 'iterator()@true, LOOP, next()@rcv.hasNext(), END_BLOCK', 218962, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (26, 'JSONObject', 'getString', 'TRY, getString(String)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 29500, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (27, 'JSONObject', 'getString', 'TRY, getString(String)@true, END_BLOCK, CATCH(JSONException), END_BLOCK', 21061, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (28, 'List', 'get', 'LOOP, get(int)@arg0<rcv.size(), END_BLOCK', 31254, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (29, 'JFrame', 'setDefaultCloseOperation', 'setDefaultCloseOperation(int)@true, setVisible(boolean)@true', 6971, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (30, 'JFrame', 'setDefaultCloseOperation', 'new JFrame(String)@true, setDefaultCloseOperation(int)@true', 5165, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (31, 'LayoutInflater', 'from', 'from(Context)@true, inflate(int,ViewGroup)@true', 8252, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (32, 'String', 'indexOf', 'indexOf(String)@true, IF, END_BLOCK', 89935, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (33, 'Activity', 'super', 'onCreate(Bundle)@true, setContentView(LinearLayout)@true', 56321, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (34, 'Double', 'parseDouble', 'TRY, parseDouble(String)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 25944, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (35, 'RandomAccessFile', 'write', 'TRY, write(byte[])@true, END_BLOCK, CATCH(Exception), END_BLOCK', 2746, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (36, 'RandomAccessFile', 'write', 'TRY, write(byte[])@true, END_BLOCK, CATCH(IOException), END_BLOCK', 2318, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (37, 'RandomAccessFile', 'write', 'TRY, write(byte[],int,int)@true, END_BLOCK, CATCH(IOException), END_BLOCK', 1591, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (38, 'RandomAccessFile', 'write', 'TRY, write(byte[],int,int)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 1578, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (39, 'RandomAccessFile', 'write', 'write(byte[])@true, close()@true', 1408, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (40, 'RandomAccessFile', 'write', 'TRY, write(byte[])@true, close()@true, END_BLOCK, CATCH(Exception), END_BLOCK', 1408, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (41, 'JFrame', 'setVisible', 'new JFrame(String)@true, setVisible(boolean)@true', 7442, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (42, 'JSONObject', 'getJSONObject', 'TRY, getJSONObject(String)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 9868, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (43, 'Cursor', 'moveToFirst', 'moveToFirst()@true, close()@true', 6502, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (44, 'SharedPreferences', 'Editor', 'edit()@true, commit()@true', 7965, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (45, 'ProcessBuilder', 'start', 'TRY, start()@true, END_BLOCK, CATCH(Exception), END_BLOCK', 4221, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (46, 'ProcessBuilder', 'start', 'TRY, start()@true, END_BLOCK, CATCH(IOException), END_BLOCK', 2283, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (47, 'ProcessBuilder', 'start', 'TRY, start()@true, getInputStream()@true, END_BLOCK, CATCH(Exception), END_BLOCK', 2256, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (48, 'ProcessBuilder', 'start', 'start()@true, getInputStream()@true', 2233, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (49, 'Activity', 'getId', 'getId()@true, IF, END_BLOCK', 344, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (50, 'PrintWriter', 'write', 'TRY, write(String)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 5034, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (51, 'PrintWriter', 'write', 'TRY, write(String)@true, close()@true, END_BLOCK, CATCH(Exception), END_BLOCK', 2473, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (52, 'PrintWriter', 'write', 'write(String)@true, close()@true', 2461, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (53, 'Cursor', 'getString', 'getString(int)@true, close()@true', 10419, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (54, 'TypedArray', 'getString', 'getString(int)@true, recycle()@true', 2126, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (55, 'TypedArray', 'getString', 'getString(int)@true, IF, END_BLOCK', 1708, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (56, 'HttpServletRequest', 'getParameter', 'TRY, getParameter(String)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 32506, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (57, 'HttpServletRequest', 'getParameter', 'getParameter(String)@true, IF, END_BLOCK', 24491, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (58, 'StringTokenizer', 'nextToken', 'nextToken()@rcv.hasMoreTokens()', 36179, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (59, 'StringTokenizer', 'nextToken', 'LOOP, nextToken()@rcv.hasMoreTokens(), END_BLOCK', 35977, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (60, 'StringTokenizer', 'nextToken', 'IF, nextToken()@rcv.hasMoreTokens(), END_BLOCK', 29418, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (61, 'StringTokenizer', 'nextToken', 'new StringTokenizer(String,String)@true, nextToken()@rcv.hasMoreTokens()', 24746, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (62, 'StringTokenizer', 'nextToken', 'new StringTokenizer(String,String)@true, LOOP, nextToken()@rcv.hasMoreTokens(), END_BLOCK', 24711, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (63, 'Jsoup', 'connect', 'TRY, connect(String)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 504, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (64, 'Jsoup', 'connect', 'connect(String)@true, get()@true', 377, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (65, 'Jsoup', 'connect', 'TRY, connect(String)@true, get()@true, END_BLOCK, CATCH(Exception), END_BLOCK', 376, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (66, 'Jsoup', 'connect', 'TRY, connect(String)@true, END_BLOCK, CATCH(IOException), END_BLOCK', 325, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (67, 'Map', 'get', 'get(String)@true, IF, END_BLOCK', 46533, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (68, 'File', 'createNewFile', 'TRY, createNewFile()@true, END_BLOCK, CATCH(Exception), END_BLOCK', 11322, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (69, 'File', 'createNewFile', 'TRY, createNewFile()@!(rcv.exists()), END_BLOCK, CATCH(IOException), END_BLOCK', 5493, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (70, 'File', 'createNewFile', 'IF, createNewFile()@!(rcv.exists()), END_BLOCK', 5483, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (71, 'URL', 'openConnection', 'TRY, openConnection()@true, END_BLOCK, CATCH(Exception), END_BLOCK', 30680, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (72, 'URL', 'openConnection', 'TRY, new URL(String)@true, openConnection()@true, END_BLOCK, CATCH(Exception), END_BLOCK', 19056, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (73, 'URL', 'openConnection', 'new URL(String)@true, openConnection()@true', 19056, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (74, 'URL', 'openConnection', 'openConnection()@true, getInputStream()@true', 16126, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (75, 'URL', 'openConnection', 'TRY, openConnection()@true, getInputStream()@true, END_BLOCK, CATCH(Exception), END_BLOCK', 16126, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (76, 'JFrame', 'getContentPane', 'new JFrame(String)@true, getContentPane()@true', 4924, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (77, 'SQLiteDatabase', 'query', 'query(String,String[],String,String[],String,String,String)@true, close()@true', 5563, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (78, 'JsonElement', 'getAsString', 'getAsString()@rcv!=null', 119, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (79, 'JsonElement', 'getAsString', 'TRY, getAsString()@rcv!=null, END_BLOCK, CATCH(Exception), END_BLOCK', 117, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (80, 'JsonElement', 'getAsString', 'getAsString()@rcv.isJsonPrimitive()', 51, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (81, 'JsonElement', 'getAsString', 'TRY, getAsString()@rcv.isJsonPrimitive(), END_BLOCK, CATCH(Exception), END_BLOCK', 48, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (82, 'ByteBuffer', 'get', 'flip()@true, get(byte[])@true', 356, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (83, 'ByteBuffer', 'get', 'LOOP, get()@rcv.hasRemaining(), END_BLOCK', 191, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (84, 'ByteBuffer', 'get', 'get()@rcv.hasRemaining()', 187, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (85, 'String', 'substring', 'substring(int,int)@arg1>=arg0', 9458, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (86, 'String', 'substring', 'substring(int,int)@rcv!=null', 9458, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (87, 'String', 'substring', 'substring(int,int)@arg1>arg0', 9458, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (88, 'String', 'substring', 'substring(int)@rcv!=null', 9188, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (89, 'String', 'substring', 'substring(int)@arg1>=arg0', 6891, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (90, 'String', 'substring', 'substring(int)@arg1>arg0', 4594, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (91, 'String', 'substring', 'substring(int,int)@rcv.length()>arg1', 3153, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (92, 'String', 'substring', 'substring(int)@arg0<rcv.length()', 2297, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (93, 'Matcher', 'find', 'matcher(String)@true, find()@true', 5851, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (94, 'String', 'getBytes', 'getBytes(String)@true', 35707, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (95, 'JFrame', 'setPreferredSize', 'new Dimension(int,int)@true, setPreferredSize(Dimension)@true', 468, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (96, 'JFrame', 'setPreferredSize', 'setPreferredSize(Dimension)@true, pack()@true', 394, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (97, 'JFrame', 'setPreferredSize', 'setPreferredSize(Dimension)@true, setVisible(boolean)@true', 390, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (98, 'JFrame', 'setPreferredSize', 'new Dimension(int,int)@true, setPreferredSize(Dimension)@true, pack()@true', 379, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (99, 'JFrame', 'setPreferredSize', 'new Dimension(int,int)@true, setPreferredSize(Dimension)@true, setVisible(boolean)@true', 359, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (100, 'JFrame', 'setPreferredSize', 'setPreferredSize(Dimension)@true, pack()@true, setVisible(boolean)@true', 342, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (101, 'JFrame', 'setPreferredSize', 'new Dimension(int,int)@true, setPreferredSize(Dimension)@true, pack()@true, setVisible(boolean)@true', 335, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (102, 'JFrame', 'setPreferredSize', 'new JFrame(String)@true, setPreferredSize(Dimension)@true', 274, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (103, 'FileInputStream', 'FileInputStream', 'TRY, new FileInputStream(File)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 72723, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (104, 'Socket', 'getOutputStream', 'TRY, getOutputStream()@true, END_BLOCK, CATCH(Exception), END_BLOCK', 17223, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (105, 'Socket', 'getOutputStream', 'TRY, getOutputStream()@true, END_BLOCK, CATCH(IOException), END_BLOCK', 9853, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (106, 'String', 'replaceAll', 'TRY, replaceAll(String,String)@rcv!=null, END_BLOCK, CATCH(Exception), END_BLOCK', 4566, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (107, 'String', 'replaceAll', 'IF, replaceAll(String,String)@rcv!=null, END_BLOCK', 4454, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (108, 'Random', 'nextInt', 'TRY, nextInt(int)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 19102, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (109, 'SortedMap', 'firstKey', 'firstKey()@rcv.size()>0', 65, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (110, 'SortedMap', 'firstKey', 'firstKey()@!(rcv.isEmpty())', 65, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (111, 'SortedMap', 'firstKey', 'firstKey()@!(rcv==null||rcv.isEmpty())', 65, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (112, 'ResultSet', 'getString', 'getString(String)@rcv.next()', 18933, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (113, 'ResultSet', 'getString', 'TRY, getString(String)@rcv.next(), END_BLOCK, CATCH(Exception), END_BLOCK', 18933, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (114, 'DataOutputStream', 'close', 'TRY, close()@true, END_BLOCK, CATCH(Exception), END_BLOCK', 16108, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (115, 'DataOutputStream', 'close', 'TRY, close()@true, END_BLOCK, CATCH(IOException), END_BLOCK', 12249, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (116, 'SimpleDateFormat', 'parse', 'TRY, parse(String)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 13581, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (117, 'SimpleDateFormat', 'parse', 'new SimpleDateFormat(String)@true, parse(String)@true', 7769, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (118, 'Scanner', 'nextLine', 'LOOP, nextLine()@rcv.hasNextLine(), END_BLOCK', 2510, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (119, 'Scanner', 'nextLine', 'TRY, nextLine()@rcv.hasNextLine(), END_BLOCK, CATCH(Exception), END_BLOCK', 2497, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (120, 'Scanner', 'nextLine', 'new Scanner(File)@true, nextLine()@true', 2300, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (121, 'ProgressDialog', 'dismiss', 'IF, dismiss()@rcv!=null, END_BLOCK', 2909, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (122, 'String', 'charAt', 'charAt(int)@arg0<rcv.length()', 27597, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (123, 'String', 'charAt', 'LOOP, charAt(int)@arg0<rcv.length(), END_BLOCK', 27083, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (124, 'Mac-String', 'doFinal-getBytes', 'TRY, getBytes()@true, doFinal(byte[])@true, END_BLOCK, CATCH(Exception), END_BLOCK', 502, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (125, 'Mac-String', 'doFinal-getBytes', 'TRY, getInstance(String)@true, getBytes()@true, doFinal(byte[])@true, END_BLOCK, CATCH(Exception), END_BLOCK', 474, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (126, 'Mac-String', 'doFinal-getBytes', 'getInstance(String)@true, getBytes()@true, doFinal(byte[])@true', 464, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (127, 'Environment', 'getExternalStorageDirectory', 'TRY, getExternalStorageDirectory()@true, END_BLOCK, CATCH(Exception), END_BLOCK', 2352, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (128, 'RandomAccessFile', 'read', 'TRY, read(byte[])@true, END_BLOCK, CATCH(Exception), END_BLOCK', 1582, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (129, 'RandomAccessFile', 'read', 'TRY, read(byte[],int,int)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 1087, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (130, 'HttpResponse', 'getEntity', 'TRY, getEntity()@true, END_BLOCK, CATCH(Exception), END_BLOCK', 10549, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (131, 'Thread', 'sleep', 'TRY, sleep(int)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 154881, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (132, 'SimpleDateFormat', 'format', 'new SimpleDateFormat(String)@true, format(Date)@true', 18977, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (133, 'InputStream', 'read', 'TRY, read()@true, END_BLOCK, CATCH(Exception), END_BLOCK', 19481, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (134, 'Matcher', 'group', 'matcher(String)@true, group(int)@true', 31511, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (135, 'Matcher', 'group', 'IF, group(int)@rcv.find(), END_BLOCK', 16447, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (136, 'Matcher', 'group', 'matcher(String)@true, IF, group(int)@rcv.find(), END_BLOCK', 14927, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (137, 'SharedPreferences', 'edit', 'edit()@true, commit()@true', 9650, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (138, 'KeyStore', 'load', 'TRY, load(InputStream,char[])@true, END_BLOCK, CATCH(Exception), END_BLOCK', 6431, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (139, 'KeyStore', 'load', 'getInstance(String)@true, load(InputStream,char[])@true', 5247, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (140, 'KeyStore', 'load', 'TRY, getInstance(String)@true, load(InputStream,char[])@true, END_BLOCK, CATCH(Exception), END_BLOCK', 5247, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (141, 'Cipher', 'init', 'TRY, init(int,Key)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 2599, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (142, 'Cipher', 'init', 'TRY, getInstance(String)@true, init(int,Key)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 1970, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (143, 'Cipher', 'init', 'getInstance(String)@true, init(int,Key)@true', 1970, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (144, 'Scanner', 'nextInt', 'new Scanner(String)@true, nextInt()@true', 3212, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (145, 'Toast', 'makeText', 'makeText(Context,String,int)@true, show()@true', 23649, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (146, 'Toast', 'makeText', 'makeText(Context,CharSequence,int)@true, show()@true', 23167, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (147, 'JSONObject', 'getJSONArray', 'TRY, getJSONArray(String)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 10391, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (148, 'JSONObject', 'getJSONArray', 'TRY, getJSONArray(String)@true, END_BLOCK, CATCH(JSONException), END_BLOCK', 7433, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (149, 'JSONObject', 'getJSONArray', 'getJSONArray(String)@true, getJSONObject(int)@arg0<rcv.length()', 3662, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (150, 'JSONObject', 'getJSONArray', 'TRY, getJSONArray(String)@true, getJSONObject(int)@arg0<rcv.length(), END_BLOCK, CATCH(Exception), END_BLOCK', 3658, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (151, 'Class', 'forName', 'TRY, forName(String)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 72394, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (152, 'File', 'mkdir', 'mkdirs()@true', 26343, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (153, 'HttpClient', 'execute', 'TRY, execute(HttpGet)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 3058, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (154, 'HttpClient', 'execute', 'new HttpGet(String)@true, execute(HttpGet)@true', 2536, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (155, 'Integer', 'parseInt', 'TRY, parseInt(String)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 143645, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (156, 'JPanel', 'add', 'new JPanel()@true, add(JButton)@true', 17652, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (157, 'String', 'split', 'IF, split(String)@rcv!=null, END_BLOCK', 20402, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (158, 'String', 'split', 'TRY, split(String)@rcv!=null, END_BLOCK, CATCH(Exception), END_BLOCK', 20402, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (159, 'LinearLayout', 'addView', 'new TextView(Context)@true, addView(TextView)@true', 1686, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (160, 'LinearLayout', 'addView', 'new LinearLayout(Context)@true, addView(LinearLayout)@true', 1325, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (161, 'LinearLayout', 'addView', 'findViewById(int)@true, addView(View)@true', 1180, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (162, 'LinearLayout', 'addView', 'addView(View)@arg0!=null', 219, FALSE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (163, 'JButton', 'addActionListener', 'new JButton(String)@true, addActionListener(ActionListener)@true', 23556, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (164, 'ApplicationInfo', 'loadIcon', 'TRY, loadIcon(PackageManager)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 453, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (165, 'ApplicationInfo', 'loadIcon', 'getPackageManager()@true, loadIcon(PackageManager)@true', 400, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (166, 'Intent', 'getStringExtra', 'getStringExtra(String)@true, IF, END_BLOCK', 6377, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (167, 'BufferedReader', 'readLine', 'TRY, readLine()@true, END_BLOCK, CATCH(Exception), END_BLOCK', 46171, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (168, 'BufferedReader', 'readLine', 'new BufferedReader(InputStreamReader)@true, readLine()@true', 31580, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (169, 'BufferedReader', 'readLine', 'TRY, new BufferedReader(InputStreamReader)@true, readLine()@true, END_BLOCK, CATCH(Exception), END_BLOCK', 31257, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (170, 'BufferedReader', 'readLine', 'TRY, readLine()@true, END_BLOCK, CATCH(IOException), END_BLOCK', 27784, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (171, 'Bundle', 'putString', 'new Bundle()@true, putString(String,String)@true', 10617, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (172, 'Cursor', 'close', 'TRY, close()@rcv!=null, END_BLOCK, CATCH(Exception), END_BLOCK', 15813, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (173, 'Cursor', 'close', 'FINALLY, close()@rcv!=null, END_BLOCK', 15732, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (174, 'PreparedStatement', 'setString', 'TRY, setString(int,String)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 36189, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (175, 'PreparedStatement', 'setString', 'prepareStatement(String)@true, setString(int,String)@true', 25954, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (176, 'PreparedStatement', 'setString', 'TRY, prepareStatement(String)@true, setString(int,String)@true, END_BLOCK, CATCH(Exception), END_BLOCK', 25954, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (177, 'PreparedStatement', 'setString', 'TRY, setString(int,String)@true, END_BLOCK, CATCH(SQLException), END_BLOCK', 25396, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (178, 'PreparedStatement', 'setString', 'TRY, prepareStatement(String)@true, setString(int,String)@true, END_BLOCK, CATCH(SQLException), END_BLOCK', 17316, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (179, 'PreparedStatement', 'setString', 'TRY, setString(int,String)@true, close()@true, END_BLOCK, CATCH(Exception), END_BLOCK', 11359, TRUE, '');
insert into patterns (id, class, method, pattern, support, isRequired, description) values (180, 'PreparedStatement', 'setString', 'setString(int,String)@true, close()@true', 11359, TRUE, '');


