//*********************************************************
//
//    Copyright (c) Microsoft. All rights reserved.
//    This code is licensed under the Apache License Version 2.0.
//    THIS CODE IS PROVIDED *AS IS* WITHOUT WARRANTY OF
//    ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING ANY
//    IMPLIED WARRANTIES OF FITNESS FOR A PARTICULAR
//    PURPOSE, MERCHANTABILITY, OR NON-INFRINGEMENT.
//
//*********************************************************

package com.microsoft.uprove;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.Properties;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TestVectorsTest extends TestCase {

	private static Properties testVectorsHash = new Properties();  
	private static Properties testVectors = new Properties();  
	private static Properties testVectorsDevice = new Properties();
	
	static {
		testVectorsHash.setProperty("hash_byte", "bf8b4530d8d246dd74ac53a13471bba17941dff7");
		testVectorsHash.setProperty("hash_octetstring", "4acd4bcea29cfee3a7d8763444f9cb10f91cab6a");
		testVectorsHash.setProperty("hash_null", "9069ca78e7450a285173431b3e52c5c25299e473");
		testVectorsHash.setProperty("hash_list", "14c8f60aa43bbe81f771c425daed8999f8fe9ede");
		
		testVectors.setProperty("UIDh", "SHA-1");
		testVectors.setProperty("UIDp", "49737375657220706172616d657465727320554944");
		testVectors.setProperty("p", "d21ae8d66e6c6b3ced0eb3df1a26c91bdeed013c17d849d30ec309813e4d3799f26db0d494e82ec61ea9fdc70bb5cbcaf2e5f18a836494f58e67c6d616480c37a7f2306101fc9f0f4768f9c9793c2be176b0b7c979b4065d3e835686a3f0b8420c6834cb17930386dedab2b07dd473449a48baab316286b421052475d134cd3b");
		testVectors.setProperty("q", "fff80ae19daebc61f46356af0935dc0e81148eb1");
		testVectors.setProperty("g", "abcec972e9a9dd8d133270cfeac26f726e567d964757630d6bd43460d0923a46aec0ace255ebf3ddd4b1c4264f53e68b361afb777a13cf0067dae364a34d55a0965a6cccf78852782923813cf8708834d91f6557d783ec75b5f37cd9185f027b042c1c72e121b1266a408be0bb7270d65917b69083633e1f3cd60624612fc8c1");
		testVectors.setProperty("S", "49737375657220706172616d65746572732073706563696669636174696f6e");
		testVectors.setProperty("y0", "3da215b335f2da6c5387fc3b14deffb28dd95da2");
		testVectors.setProperty("g0", "7b00fa156976f20ddff256cbbd039017f42b3106f00d6a1b2381b382dd0aaad82230a04cd4769c984519334e62ae0763883b770157717747d1dd21f9c15033717f46b729185ef765c9bf405ffd2095a5c710a13f8e7ab7e575d0ff00c8965fc806306dc7d0d2e273095776c83a09e074d48942a4c76982f36c381e615daa632e");
		testVectors.setProperty("z0", "6ad61dbc3c05c27268cb770cc87ddad12592dedab9f5b3a6027b49aaabdd1d2910d70f37c3ba50b324e61943d23329e93062c776ee074a16ce8522263d4d096d9d62c5045f6904cf38dc8a8431a8ed3a95c972ec9cfd43fc21c9d3004f48f206207306e5bae99d97df04c5d5304084dfd9423296a086a8b684640167175a0339");
		testVectors.setProperty("y1", "1605f1b844fc20b1ccde07903df548f637a0c8c7");
		testVectors.setProperty("g1", "5a6c337c859a54d89ef627731fb61a1e2aad5fd1dbbb0f9babc3488539e0a62c2df7e9adf6755f8f6d6351cbcab01091dee4272aa4e6d6912902a98b4b4fe0024df2924065d43233dc7240a1f24703ffbddab3a34b3af508dc4710dd9bfeb597253feeaa331eabfc524f44e2310691e82a85616060de3bcf8c5e536ffe25ee27");
		testVectors.setProperty("z1", "30148991a0188e75b150bf41a1a82af12b1c5f3089a048682a1bfce6a867520fa3e1d7d23b76f3e87116e194e3273aa6d5c11e2b709f1f4a6c0066a52fb415c713b3462317005575e440951453b2344ce225bd69907cbd08b51f3fb8a9d62bd1fb62531db7fb1cf3199226111e834061819374f9583325bbd4f4c56b30eef0fd");
		testVectors.setProperty("y2", "efc4d8cf1ac4d6fe83cb41517ec8bc3122fa75fd");
		testVectors.setProperty("g2", "3d08cc61891abbe632cb8b7d25fe7f94b1191bd4987c5fe94447827a9e72fa60da5882958afc2c0e74d5b1a61582766be42ddc86d6028efedebf012bd73493f4a178f97a558951c4e7404b1d0a735e5e25d348d7ef4bb64877a22e640106f4b7e8ce63b4bdb7b045f9445fa4ff08806b79cfe563e51514c0120cdd3353fb708");
		testVectors.setProperty("z2", "76490e9eda0bb8a3dc5406fdb1a1e18c1a04386a77aecf94c5e43eb3d0bc7124cdcdaf379f757aada87fa6dfe8481e5a0bc25ec021090136ece842fd0d73754ac2396646865a86b3b960c8d54855e9cb91b132ad361f3c6db7b8bef794353d82baf00ded0fb2c2333d14ce4cbe42046c9d5e2f18a5253f61f6005e6dfe8baf2c");
		testVectors.setProperty("y3", "bf9493f74fa9d19bfefe6effff18f8117d1d94f2");
		testVectors.setProperty("g3", "b68249ae5cd4b52b92426327b9ea4671f3ad1993fcc93455c79724ed4254bc0e4154fce5e72fe6a4b05db65d39196b495035bf4e40a6a58b02826784772ef0ac0fd33595139028c0303bedc64062ccbdef8057e024e282868f68cb83ea88b640d8c919b3669a2a74f864ebbbd878a72eaf49814d479f8465684437d173ba6d3a");
		testVectors.setProperty("z3", "cc8286bc1202d1d83d18cbf66082300fe93a3ea6287a58509790567017c5c1529c5b06861c01d0cdee54f0e7bdd78937284b0063c8c0ef3423a9fecd2960ce436e4d63ac38a412be6eceeb11d7aa13ef0a3018662dc4f4c3771fa0770961e56470372f1a404030b49d02392cabb1455c80a154988832331b39f53f80bfa77284");
		testVectors.setProperty("y4", "1e930cf37b99d3bc922335902951f1439f4d546c");
		testVectors.setProperty("g4", "1529d2ed7dad0e23d67de9745c81cc632962181fdc6b17da3e6447cb0d2f14708d48f50ad9c4ff2f35a92611be66e567c7ff9d0c36884c5e1b220d343759e432cd94c8e2c9787c93a585a11d6a881a2b78e903883b3ff42d5fcc5d6611870b8e137e4127acb3e30e8cc65da7cf4781215a5930e10e6bdcb64160cb9de1f12e36");
		testVectors.setProperty("z4", "cfbaf2a3a3c872e988edb09eb4fa9dc7c3f210571e504b389175dfc459e8b837ea6fccd5b95197a07be629963fab489d70a3858821af979b6ce4cc60dfdb90d77ad3e205a33e44c6336ec11e2f492bbace97cb56b9f38fcd98fc70cda5fa30527ce83317f1aa43f119637243bbdba6ba40130bd86e77159c24355dc8626de3e5");
		testVectors.setProperty("y5", "161b89af22bd038698c56cbe58880cb6fa424079");
		testVectors.setProperty("g5", "813ec484c590589f63474ff7097c54c113dea640e2ef1c9ba509769aad2ada1310087647aa11507fae3e81953904ad827579268dc0ebc7afaaab4b92436d6b1e1b0102d354c9300cc0e165d3ffa69a683ddb76577013cdafb951fdd7ae580057f91319d2be8ec6bd2c8009535b44b591f689157d1c0d41ce1830947a1c1b78c");
		testVectors.setProperty("z5", "42860677300ecd7214471314fc37501d090c125e39f0cae52edce8ed7b5020d76d74fbc615acafdbe14e8e7cddebc7d12c5ae4b79f9221ab2bbbee4f7934c22106e07eab6d7f59b53283ca5349f1ad1580b32c6f1a5390b07c216aedd2a3300152f17fffcb32bb7f10aa7d55d6de5e9ab4099785cde7bf929d76c3ae81219280");
		testVectors.setProperty("yt", "c969d8d1a358babaa5b2e23f7a144017b62d831b");
		testVectors.setProperty("gt", "787932da157a26ca6f7f89322f2359b5075584c7d229502002b0cec0b805830091e38c13ad2353a4d067999c0254e04693d1b1f64e0f380f8ee96de6072bcfdaba69a1881642e436dcf42a490c8215a3eb0fafb084d8bf70237854cebef1c57a96c6618bcb6d3b88f9fb4afe5fcaaa1ab5d1fd54d8e893d56298060df385bad4");
		testVectors.setProperty("zt", "c7699f6f884f567acd48e1a8a35e28dea0784064a386c5b2327fbe8e277b56d01b12be6dc0c35af7d1eb854cf001409a16e0ddb7dda9f3857f27b3053fb5735c74ddbae59b3cbdaf7688148e71be4f54a40b813973c95faa271bca877b10aab5d2d13e92c1b1d2c6756d6b910610e52acf63cec3c27cfedad98f9342cb8fdfd9");
		testVectors.setProperty("A1", "416c69636520536d697468");
		testVectors.setProperty("e1", "1");
		testVectors.setProperty("A2", "5741");
		testVectors.setProperty("e2", "1");
		testVectors.setProperty("A3", "313031302043727970746f20537472656574");
		testVectors.setProperty("e3", "1");
		testVectors.setProperty("A4", "01");
		testVectors.setProperty("e4", "0");
		testVectors.setProperty("A5", "499602d2");
		testVectors.setProperty("e5", "0");
		testVectors.setProperty("TI", "546f6b656e20696e666f726d6174696f6e206669656c642076616c7565");
		testVectors.setProperty("PI", "50726f76657220696e666f726d6174696f6e206669656c642076616c7565");
		testVectors.setProperty("x1", "9cf4ab0d76c4fe30e7b0c2858683cc11a40d6b95");
		testVectors.setProperty("x2", "7fb73ae5906b370b90923024f92eb5a285266311");
		testVectors.setProperty("x3", "1547914c42bbb26c6474d94bd6bfcf624318c9fe");
		testVectors.setProperty("x4", "1");
		testVectors.setProperty("x5", "499602d2");
		testVectors.setProperty("P", "f5c06c9f2f19c7daf97632d09b484ec66521513a");
		testVectors.setProperty("xt", "f0e726849b9aaac3e39043c0666e513f3ef2197f");
		testVectors.setProperty("gamma", "d14cf8a7b66c27148cee98c6c217e4752ab6aa55a3d79483e20751ed71ae35caed505c67c8c09ac87900b2591d92e7a9697eb0116eca6896354d73ec51bc7aa348a059980be734e408d498fadf001a50bc1659d616c8217fa21a1ac3c6494db0ab58ca1c042f009ab39e52387bf69b2854eee64c0cf4cc819202cd6aac3e6666");
		testVectors.setProperty("sigma_z", "3b705c88b681c79632794d2fbb5f365a1f1ae773a616cc5775c4b3bce9d98c8979f85386b7ebe17e19703727ab723fd01a94b2550f0e56c70c9b79bec22374b926052063f19626a75cc795836eba23c324da8d945a1d700d23cf4ba7a9a74addecb6994915c192d2aedf976ee3dbc23b4952b1f08a10f0157a9ca975bd96217a");
		testVectors.setProperty("alpha", "f898a509e2382712d92892e92ca3679d482a12bc");
		testVectors.setProperty("beta1", "e203fb59767ce9591c14296d662d00856c1a577");
		testVectors.setProperty("beta2", "9910ca3dd6f9cad2e9f4acd6ceb04678d1393936");
		testVectors.setProperty("h", "f96e9648f8a39ea9dae051c6265a98c269bac00ffcb013da4ef9555ce108ed6687b8e2aff83e82caddf4bba971ba88d7fbbc14880c2d06694c141e1feb28715ac2ad33b1a8b86ab7840503a33faf4f6098c825384395d0e065974fa5b91903f5813b01517ac6f1bd72837e7934103d186fd5c0362f9ce12513619f9bbf88128");
		testVectors.setProperty("sigma_z_prime", "62c88746e4ce2da965aa55e51e0cd9aee97f42f5603659a075bfcbb3a2481920862210c3542a7848aa5fced32545c54badd608913c8cdd70f040caf64dfb76c08b725306854da34a81b580dd097a5498faab17ef11b913ce4b4e66945253c89378745067bb256672b8cc1a88efb17b61895291640bd81152a958897cc01c5090");
		testVectors.setProperty("alphaInverse", "b8ad2efa2cf1822a67029a0bb6f4061e32881eca");
		testVectors.setProperty("w", "658dd43ba7469163dc4a180cab2cf6f80c956f67");
		testVectors.setProperty("sigma_a", "b78d222e0ec5c0f6fbace8fce68d0b5495812d661a4b0e2281c6bda391a53212a5e29dc98157771378233e434a7dce282da8f5537349d27072976c52c799e4974a34f14b739d2abd62b758bfbb01d66860bff97961d059ccfefeee9feb4f7e1162eeb55d0b33eb10a2379bdbb93e7e74e5107788dea53d6ce1db684727dc3c0");
		testVectors.setProperty("sigma_b", "9528b9bdb52019177c8677591af38af87b3a30bd5f02c019418d302f83ae113287f88506bf37fdc07a32760a95957e35d0f8d26288b9231598346870e2819101ae62995f2537442713d24b5e2218d3f6b4e5def0e90f2dc38a0f4f0c3440e8d443c558722e8e72437eab823717424d7460064d2721e0d8d2e11bc05876605cb");
		testVectors.setProperty("sigma_a_prime", "198f77cac135dc4e99c544a952970c17edbea2a7114b6a0285aa73399c00033dded47be0227d539afc5c38834c3a98212c6a823950ee0ad172f6ddc3152e6fddd8c0147422ceece11fa7336b281e1d555498a32eb3d76d3af06e7447b30d34cfdf8f695cb2c70792a2088027e30740e7d30d5eea7fe937210e14daff34d71fd8");
		testVectors.setProperty("sigma_b_prime", "8686a0725cf8122258d5c5a4f05d46d6b3f0734c41cb06a2615be4c1b4f3dd87d37ef34a3fc385875572466962b05cd9d59b8ad93a59de621a92fc8b73cf66b0e250578b399612e9ac9d36fe66dc11158d20f813551bf70f192bf8073f70d5acc5eb80555db04f16c2e3b6c37e1b19a1e3b8509d2bfd2416cbe373349b9fe65a");
		testVectors.setProperty("sigma_c_prime", "ec8e8d40c7ff28845913974a1afa720633ab96d1");
		testVectors.setProperty("sigma_c", "faaeccf65f66f719ead4d9e0f15d420e8a6d3c48");
		testVectors.setProperty("sigma_r", "26bc5253101892743f0fd5405db000a86be54e90");
		testVectors.setProperty("sigma_r_prime", "bfcd1c90e7125d47290482172c6047213d1e87c6");
		testVectors.setProperty("D", "2,5,");
		testVectors.setProperty("U", "1,3,4,");
		testVectors.setProperty("m", "56657269666965725549442b72616e646f6d2064617461");
		testVectors.setProperty("w0", "3d61b74db035947d3424778278a60d3380e66426");
		testVectors.setProperty("w1", "8930689e839f5246925bd89acf07a0bbe11bad36");
		testVectors.setProperty("w3", "90bc6f73c49bec43518aa22f8f2672344a711890");
		testVectors.setProperty("w4", "25129638a63dbb6b17fd4552e7706c1ce93f7418");
		testVectors.setProperty("a", "a8fe25d196ca6630be65bd6b50d76146b641e1e1");
		testVectors.setProperty("UIDt", "7310b6bfd30e67b8b875bc35381fe8c26139b2c2");
		testVectors.setProperty("F", "50e8c1923369fd0d6f8d48045f1818841cc245d2");
		testVectors.setProperty("c", "6943860f0ddc0de133fb296373f2b1cdec3eae9b");
		testVectors.setProperty("r0", "8c3fab034b52834e4225e9144dd7d79ec70e8477");
		testVectors.setProperty("r1", "e5e2bb77f228db897bc2d7b665dac55e2c08a726");
		testVectors.setProperty("r3", "103f607ac880854aa0927f20ca47dbfd48f6e1b3");
		testVectors.setProperty("r4", "bbc71b0b361069ebd865729e7cb3965d7e15542e");
		
		testVectorsDevice.setProperty("UIDh", "SHA-1");
		testVectorsDevice.setProperty("hash_byte", "bf8b4530d8d246dd74ac53a13471bba17941dff7");
		testVectorsDevice.setProperty("hash_octetstring", "4acd4bcea29cfee3a7d8763444f9cb10f91cab6a");
		testVectorsDevice.setProperty("hash_null", "9069ca78e7450a285173431b3e52c5c25299e473");
		testVectorsDevice.setProperty("hash_list", "14c8f60aa43bbe81f771c425daed8999f8fe9ede");
		testVectorsDevice.setProperty("UIDp", "49737375657220706172616d657465727320554944");
		testVectorsDevice.setProperty("p", "d21ae8d66e6c6b3ced0eb3df1a26c91bdeed013c17d849d30ec309813e4d3799f26db0d494e82ec61ea9fdc70bb5cbcaf2e5f18a836494f58e67c6d616480c37a7f2306101fc9f0f4768f9c9793c2be176b0b7c979b4065d3e835686a3f0b8420c6834cb17930386dedab2b07dd473449a48baab316286b421052475d134cd3b");
		testVectorsDevice.setProperty("q", "fff80ae19daebc61f46356af0935dc0e81148eb1");
		testVectorsDevice.setProperty("g", "abcec972e9a9dd8d133270cfeac26f726e567d964757630d6bd43460d0923a46aec0ace255ebf3ddd4b1c4264f53e68b361afb777a13cf0067dae364a34d55a0965a6cccf78852782923813cf8708834d91f6557d783ec75b5f37cd9185f027b042c1c72e121b1266a408be0bb7270d65917b69083633e1f3cd60624612fc8c1");
		testVectorsDevice.setProperty("S", "49737375657220706172616d65746572732073706563696669636174696f6e");
		testVectorsDevice.setProperty("y0", "3da215b335f2da6c5387fc3b14deffb28dd95da2");
		testVectorsDevice.setProperty("g0", "7b00fa156976f20ddff256cbbd039017f42b3106f00d6a1b2381b382dd0aaad82230a04cd4769c984519334e62ae0763883b770157717747d1dd21f9c15033717f46b729185ef765c9bf405ffd2095a5c710a13f8e7ab7e575d0ff00c8965fc806306dc7d0d2e273095776c83a09e074d48942a4c76982f36c381e615daa632e");
		testVectorsDevice.setProperty("z0", "6ad61dbc3c05c27268cb770cc87ddad12592dedab9f5b3a6027b49aaabdd1d2910d70f37c3ba50b324e61943d23329e93062c776ee074a16ce8522263d4d096d9d62c5045f6904cf38dc8a8431a8ed3a95c972ec9cfd43fc21c9d3004f48f206207306e5bae99d97df04c5d5304084dfd9423296a086a8b684640167175a0339");
		testVectorsDevice.setProperty("y1", "1605f1b844fc20b1ccde07903df548f637a0c8c7");
		testVectorsDevice.setProperty("g1", "5a6c337c859a54d89ef627731fb61a1e2aad5fd1dbbb0f9babc3488539e0a62c2df7e9adf6755f8f6d6351cbcab01091dee4272aa4e6d6912902a98b4b4fe0024df2924065d43233dc7240a1f24703ffbddab3a34b3af508dc4710dd9bfeb597253feeaa331eabfc524f44e2310691e82a85616060de3bcf8c5e536ffe25ee27");
		testVectorsDevice.setProperty("z1", "30148991a0188e75b150bf41a1a82af12b1c5f3089a048682a1bfce6a867520fa3e1d7d23b76f3e87116e194e3273aa6d5c11e2b709f1f4a6c0066a52fb415c713b3462317005575e440951453b2344ce225bd69907cbd08b51f3fb8a9d62bd1fb62531db7fb1cf3199226111e834061819374f9583325bbd4f4c56b30eef0fd");
		testVectorsDevice.setProperty("y2", "efc4d8cf1ac4d6fe83cb41517ec8bc3122fa75fd");
		testVectorsDevice.setProperty("g2", "3d08cc61891abbe632cb8b7d25fe7f94b1191bd4987c5fe94447827a9e72fa60da5882958afc2c0e74d5b1a61582766be42ddc86d6028efedebf012bd73493f4a178f97a558951c4e7404b1d0a735e5e25d348d7ef4bb64877a22e640106f4b7e8ce63b4bdb7b045f9445fa4ff08806b79cfe563e51514c0120cdd3353fb708");
		testVectorsDevice.setProperty("z2", "76490e9eda0bb8a3dc5406fdb1a1e18c1a04386a77aecf94c5e43eb3d0bc7124cdcdaf379f757aada87fa6dfe8481e5a0bc25ec021090136ece842fd0d73754ac2396646865a86b3b960c8d54855e9cb91b132ad361f3c6db7b8bef794353d82baf00ded0fb2c2333d14ce4cbe42046c9d5e2f18a5253f61f6005e6dfe8baf2c");
		testVectorsDevice.setProperty("y3", "bf9493f74fa9d19bfefe6effff18f8117d1d94f2");
		testVectorsDevice.setProperty("g3", "b68249ae5cd4b52b92426327b9ea4671f3ad1993fcc93455c79724ed4254bc0e4154fce5e72fe6a4b05db65d39196b495035bf4e40a6a58b02826784772ef0ac0fd33595139028c0303bedc64062ccbdef8057e024e282868f68cb83ea88b640d8c919b3669a2a74f864ebbbd878a72eaf49814d479f8465684437d173ba6d3a");
		testVectorsDevice.setProperty("z3", "cc8286bc1202d1d83d18cbf66082300fe93a3ea6287a58509790567017c5c1529c5b06861c01d0cdee54f0e7bdd78937284b0063c8c0ef3423a9fecd2960ce436e4d63ac38a412be6eceeb11d7aa13ef0a3018662dc4f4c3771fa0770961e56470372f1a404030b49d02392cabb1455c80a154988832331b39f53f80bfa77284");
		testVectorsDevice.setProperty("y4", "1e930cf37b99d3bc922335902951f1439f4d546c");
		testVectorsDevice.setProperty("g4", "1529d2ed7dad0e23d67de9745c81cc632962181fdc6b17da3e6447cb0d2f14708d48f50ad9c4ff2f35a92611be66e567c7ff9d0c36884c5e1b220d343759e432cd94c8e2c9787c93a585a11d6a881a2b78e903883b3ff42d5fcc5d6611870b8e137e4127acb3e30e8cc65da7cf4781215a5930e10e6bdcb64160cb9de1f12e36");
		testVectorsDevice.setProperty("z4", "cfbaf2a3a3c872e988edb09eb4fa9dc7c3f210571e504b389175dfc459e8b837ea6fccd5b95197a07be629963fab489d70a3858821af979b6ce4cc60dfdb90d77ad3e205a33e44c6336ec11e2f492bbace97cb56b9f38fcd98fc70cda5fa30527ce83317f1aa43f119637243bbdba6ba40130bd86e77159c24355dc8626de3e5");
		testVectorsDevice.setProperty("y5", "161b89af22bd038698c56cbe58880cb6fa424079");
		testVectorsDevice.setProperty("g5", "813ec484c590589f63474ff7097c54c113dea640e2ef1c9ba509769aad2ada1310087647aa11507fae3e81953904ad827579268dc0ebc7afaaab4b92436d6b1e1b0102d354c9300cc0e165d3ffa69a683ddb76577013cdafb951fdd7ae580057f91319d2be8ec6bd2c8009535b44b591f689157d1c0d41ce1830947a1c1b78c");
		testVectorsDevice.setProperty("z5", "42860677300ecd7214471314fc37501d090c125e39f0cae52edce8ed7b5020d76d74fbc615acafdbe14e8e7cddebc7d12c5ae4b79f9221ab2bbbee4f7934c22106e07eab6d7f59b53283ca5349f1ad1580b32c6f1a5390b07c216aedd2a3300152f17fffcb32bb7f10aa7d55d6de5e9ab4099785cde7bf929d76c3ae81219280");
		testVectorsDevice.setProperty("yt", "c969d8d1a358babaa5b2e23f7a144017b62d831b");
		testVectorsDevice.setProperty("gt", "787932da157a26ca6f7f89322f2359b5075584c7d229502002b0cec0b805830091e38c13ad2353a4d067999c0254e04693d1b1f64e0f380f8ee96de6072bcfdaba69a1881642e436dcf42a490c8215a3eb0fafb084d8bf70237854cebef1c57a96c6618bcb6d3b88f9fb4afe5fcaaa1ab5d1fd54d8e893d56298060df385bad4");
		testVectorsDevice.setProperty("zt", "c7699f6f884f567acd48e1a8a35e28dea0784064a386c5b2327fbe8e277b56d01b12be6dc0c35af7d1eb854cf001409a16e0ddb7dda9f3857f27b3053fb5735c74ddbae59b3cbdaf7688148e71be4f54a40b813973c95faa271bca877b10aab5d2d13e92c1b1d2c6756d6b910610e52acf63cec3c27cfedad98f9342cb8fdfd9");
		testVectorsDevice.setProperty("yd", "89db73d0edbd55957aac82677243f40ba40a42e2");
		testVectorsDevice.setProperty("gd", "98ce0e12dc74c4d8f4d79e1aa65db2f622c7cc75d47d880c4c1f8bb2dec82caeeea63292c5e478422ac27d0a7afdd1b57b326755b8ea6367606ad8fbcd4833614d1a63789d8a91458918f030683777ccd3740ad6ec9add3d6de6a499d5ac3f4f988662bbe411dd65438bc94ff3c219f6fc017cc63eefc18671fe12f99000f48f");
		testVectorsDevice.setProperty("zd", "2f67389ccc7796de9c8dd1a5863e541d5c1a7da3cc96f7e6f50634c32b4c9a3cb0acf5a69d590efc8c046676b0cc6b13d3e40833444dadfec6b787f2212577c1794aa9b0a9a686f3f642b5664f0eb90d2eb25c4b64cf84d6a20460ec313e8577368969171232d7e3006cee25eaf5d69d9bfab63b97111d2e1d35a0ae939cf401");
		testVectorsDevice.setProperty("A1", "416c69636520536d697468");
		testVectorsDevice.setProperty("e1", "1");
		testVectorsDevice.setProperty("A2", "5741");
		testVectorsDevice.setProperty("e2", "1");
		testVectorsDevice.setProperty("A3", "313031302043727970746f20537472656574");
		testVectorsDevice.setProperty("e3", "1");
		testVectorsDevice.setProperty("A4", "01");
		testVectorsDevice.setProperty("e4", "0");
		testVectorsDevice.setProperty("A5", "499602d2");
		testVectorsDevice.setProperty("e5", "0");
		testVectorsDevice.setProperty("TI", "546f6b656e20696e666f726d6174696f6e206669656c642076616c7565");
		testVectorsDevice.setProperty("PI", "50726f76657220696e666f726d6174696f6e206669656c642076616c7565");
		testVectorsDevice.setProperty("x1", "9cf4ab0d76c4fe30e7b0c2858683cc11a40d6b95");
		testVectorsDevice.setProperty("x2", "7fb73ae5906b370b90923024f92eb5a285266311");
		testVectorsDevice.setProperty("x3", "1547914c42bbb26c6474d94bd6bfcf624318c9fe");
		testVectorsDevice.setProperty("x4", "1");
		testVectorsDevice.setProperty("x5", "499602d2");
		testVectorsDevice.setProperty("P", "39c4515dd5fb1637f1ccf7e99ea9625bc83ded39");
		testVectorsDevice.setProperty("xt", "762a6442ab8ac6797538a96dedfc50875ae9b7b1");
		testVectorsDevice.setProperty("xd", "3660200d0f3a1f1636a36d12a88e45c6bec7659e");
		testVectorsDevice.setProperty("hd", "10459e650e333ba7add82c0a6c0772972b3de8c6cd88647bab6ea29cd4f1e1c912121893427b776c542b7f0f2f754665a14ea90f3edd66d6fd5a77209c0f17ee260181ccc4bbf0a0ac1858259e32a96c9cefa92d4ef37470218827253af8508b067b687d01206424b0773e3301cb8cff250dead9a2f52a34e491051f4fee07b9");
		testVectorsDevice.setProperty("zeta_d", "b45099675a81ab5bd446d34b14250b4065ca410df60fc6b6fead6db8a2b32036e96d366f85b6877cfe6bf480dd05b0fac614ba9ed1359f14fcd3f7f0a40e2d30d83f8313bea2dfa155b5e4a14f7f3ec916fb22a0d8476990a86603d25ba562557526e1cb4a86c8286c554d13c753c0482b353dbf1a3da90ff82507fb12f928e1");
		testVectorsDevice.setProperty("gamma", "27442e9351f952fe7c8b55147b778dd0730ac5651ed40cd272e2749dfc244d7f33ec59040c73babc2e1cb3593b4801c938fa1946c76c2c656289947d9ae213d0b9eec21288db801ab44d163736a7ac9f5a4b7cb8463aacc25688af6325378642cc4e22d4832785b7504b51addb974196b9bbb3398aaad8d3dfe824957b11547a");
		testVectorsDevice.setProperty("sigma_z", "2bfe1f799e30263290bb960cbd4494993dfa635c30c577c399032aaa4b1610cc9f0207bde943b69d806215be45829699cee4225d99102d3b6ac3e56cdb73fcc26afc2d1c05b9127ffb84345f57a2d82f58301c192a8cb32747e32a31b8ddd7c4426103efc8246aa16991f6676f0237793965e874880511a78b5f493c668c13bc");
		testVectorsDevice.setProperty("alpha", "cc70cf54d1a5c3fddbc3a36a2acd65fae6f78724");
		testVectorsDevice.setProperty("beta1", "a9a170d25b0a7a28e956ec7db369111f1784db67");
		testVectorsDevice.setProperty("beta2", "84fd77ef09680500abaf45492f9aa862d02f2d42");
		testVectorsDevice.setProperty("h", "ac9743910ba8c41a6d865eb7d194b08449b31932b462e805c15251cd6142170948956ab6acec618bef079ce088a3a4801bd7aed87dd70bc6b31354cdf68606d5fce389837788ca146a9c330030c3528ce3f89ae394d9d6e8bc191eca2392da63f5195a425d36972b38fe49b412bae1faacfaadf3a626539dec0b70da627ffdc3");
		testVectorsDevice.setProperty("sigma_z_prime", "97738dc67ce8371636fabd0a9712cabb68a0ef6a0e4f94ac5204eda355c9a1b6b54a7578619d9db53d2c7ec44b5be4ff14a4381ee47da5095f7c83913929189559dddcabd3839bec3f36ea13cd16b9f246a97de8d85728544c612a30d27634e5162c4dd3f619c2422eac83f8ebfaf61439749d78405d90ac6f1b04080689d21");
		testVectorsDevice.setProperty("alphaInverse", "a2f7493f0763682f78fee9ccc8fcf6bb92747a3d");
		testVectorsDevice.setProperty("w", "ed8c42bbbccb6515f9c066764d8a5316adbdde8c");
		testVectorsDevice.setProperty("sigma_a", "6c74165662348361b0e899b89868191d6270e4726dcb51955daee6648078fc8a753fe49ba8a564cc177ade6b412079518833237b39f1193a108135f65b69d94da0179fe3f53aab8470d5d0de8324e673b8250dcf51717325b2d59f688fe4e375f765b2a2504a01c5dac7f43b0ecda6b570f38749ec40bd72044a9f62168d1c8c");
		testVectorsDevice.setProperty("sigma_b", "87064809613f96519abf7423e4b9f41bb273103552d53b706d7e130833e93dbdd1b35ac0ab4f36c11caf0a98fdd96e20239688d9be599cb6d4d1a62f8081cfe826198757d09a007d062fe7238f029301ce31f7ebe87dcc0ba87cfd20c3c55f9e9ab4b5f1e5ea8e64d118171560c64ddc57f025abe31006c83ba7bb1f49556983");
		testVectorsDevice.setProperty("sigma_a_prime", "b30f9fc6b6d1ab7a1cabf284afd8f48c81f098b645a01c83e64d7e53c53d63349e1758f233895cca234280a2fe33f27d62c9c9ebefbc9dc5c9c02856a71ef2148c9b32edf729ddb51ceb007058d7234177060f17a287ba527f74405ca9b497b051389e2dc3baa974b808e4e924e1b5d02bd84150d89040f9028ae3546aafe498");
		testVectorsDevice.setProperty("sigma_b_prime", "d0df6f20b2dfc027d3a7809048b11b1a2af0bc686e8756cf344b8c399bff112720ef42342416ff333a7d8fd9fd08aef62bc57bb9aba54bfc0e7abf0bab0d9db744b9332b7e642d85467a22b773a3c1c53a4224d7549dba3b003eee58774a33b07891ea4080f19136993f2b1349ede0115a8a74bab0b0e84b604e546309c23ef6");
		testVectorsDevice.setProperty("sigma_c_prime", "bac0ce8092f93a207a688ca0bbc81eabe870758d");
		testVectorsDevice.setProperty("sigma_c", "646a34715054f7e76f5c226f65fb53bc7ee0c243");
		testVectorsDevice.setProperty("sigma_r", "58d0ffb459dae6749610ed2af90c0337709a0f66");
		testVectorsDevice.setProperty("sigma_r_prime", "ddce77a36342eb7541c0327428a6ab9a40c93ca8");
		testVectorsDevice.setProperty("D", "2,5,");
		testVectorsDevice.setProperty("U", "1,3,4,");
		testVectorsDevice.setProperty("m", "56657269666965725549442b72616e646f6d2064617461");
		testVectorsDevice.setProperty("md", "446576696365206d657373616765");
		testVectorsDevice.setProperty("w0", "be02077f78b234d7fb26d6934d8c6f7366b7004f");
		testVectorsDevice.setProperty("wd", "a5811941cd4028531df4c60ec8f93090ff055517");
		testVectorsDevice.setProperty("wdPrime", "9db1f81912cf453ffa7bc840d664c7bf99bd3a34");
		testVectorsDevice.setProperty("ad", "be8330110ca6c4eeb23e51990d073fb093a5292831129aa56b6e82990f8e6c2195f5958a821f44440a262de94ac3fe41e5412fdb20459af2bc71299b201abcc76501fa8b0ef0eb2e31d5401ac591ea4f5aae8118bb63b4e4a01332180f15937e401f357d43bb45c1ba5fbff301006232190fcafffd09171c96969f0c8303e46a");
		testVectorsDevice.setProperty("w1", "b802545aa29e08d2feba6c2d8836ba27e05d337c");
		testVectorsDevice.setProperty("w3", "fafc3608df4e8d86785bd3c467b919549d37e828");
		testVectorsDevice.setProperty("w4", "1f1186f082035b790c2046e80a19dae17a95bdad");
		testVectorsDevice.setProperty("a", "629e6619c2528c933987cfd2504613e05e5cbadc");
		testVectorsDevice.setProperty("UIDt", "aab4198f58fd9b2e889d12f0cc047fb8c6499892");
		testVectorsDevice.setProperty("F", "50e8c1923369fd0d6f8d48045f1818841cc245d2");
		testVectorsDevice.setProperty("c", "165ff4bc50972698b308108aa6870d7d124d5d9");
		testVectorsDevice.setProperty("mdPrime", "5c68397eef82a92d18c14ef47f3900d005714e89");
		testVectorsDevice.setProperty("r0", "e56a1c9aec88307bfa861cc3ecaf13c2ba3f3ba4");
		testVectorsDevice.setProperty("r1", "6a4e9d02857f3f9eb0bd21a0dc9ee6c53b2c481f");
		testVectorsDevice.setProperty("r3", "81a9ef30ccf7b08fa33d5b8943eaa474f50c4bf0");
		testVectorsDevice.setProperty("r4", "1dab87a4bcf9e90f80efc5df5fb16a09a970e7d4");
		testVectorsDevice.setProperty("rdPrime", "9b30f801542ebe75ccb6356e3f47fc7228dcfb77");
		testVectorsDevice.setProperty("rd", "40ba066183c02a66f647a4cdff0b50f4a6cdc1dd");
	}

	/**
	  * Constructor for TestVectorsTest.
	  * @param arg0
	 * @throws IOException 
	 * @throws FileNotFoundException 
	  */
    public TestVectorsTest(String arg0) throws FileNotFoundException, IOException {
        super(arg0);
    }

	// tests U-Prove Cryptographic Specification Hash Formatting test vectors (section 4.1)
    public void testHashFormattingTestVectors() throws NoSuchAlgorithmException, NoSuchProviderException {
    	
        HashFunction sha1 = HashFunctionImpl.getInstance("SHA-1", FieldZqTest.Zq160);
        
    	byte b = (byte) 1;
    	sha1.update(b);
    	Assert.assertTrue(Arrays.equals(TestUtils.hexToBytes(testVectorsHash.getProperty("hash_byte")), sha1.getByteDigest()));
    
    	byte[] octetString = new byte[] {(byte) 1,(byte) 2,(byte) 3,(byte) 4,(byte) 5}; 
    	sha1.update(octetString);
    	Assert.assertTrue(Arrays.equals(TestUtils.hexToBytes(testVectorsHash.getProperty("hash_octetstring")), sha1.getByteDigest()));

    	sha1.updateNull();
    	Assert.assertTrue(Arrays.equals(TestUtils.hexToBytes(testVectorsHash.getProperty("hash_null")), sha1.getByteDigest()));

    	sha1.update(3); // list length
    	sha1.update(b);
    	sha1.update(octetString);
    	sha1.updateNull();
    	Assert.assertTrue(Arrays.equals(TestUtils.hexToBytes(testVectorsHash.getProperty("hash_list")), sha1.getByteDigest()));
    }

    private IssuerParameters loadIssuerParameters(boolean device) throws IOException {
    	IssuerParameters ip = new IssuerParameters();
    	Properties properties = device ? testVectorsDevice : testVectors;
    	
    	ip.setParametersUID(TestUtils.hexToBytes(properties.getProperty("UIDp")));
    	
    	BigInteger p = new BigInteger(1, TestUtils.hexToBytes(properties.getProperty("p")));
    	BigInteger q = new BigInteger(1, TestUtils.hexToBytes(properties.getProperty("q")));
    	BigInteger g = new BigInteger(1, TestUtils.hexToBytes(properties.getProperty("g")));
    	Subgroup group = new Subgroup(p,q,g); 
    	ip.setGroup(group);
    	
    	ip.setHashAlgorithmUID(properties.getProperty("UIDh"));

    	int i = 0;
    	byte[][] publicKey = new byte[device ? 8 : 7][];
    	byte[][] proverIssuanceValues = new byte[device ? 8 : 7][];
    	publicKey[i] = TestUtils.hexToBytes(properties.getProperty("g0"));
    	proverIssuanceValues[i++] = TestUtils.hexToBytes(properties.getProperty("z0"));
    	publicKey[i] = TestUtils.hexToBytes(properties.getProperty("g1"));
    	proverIssuanceValues[i++] = TestUtils.hexToBytes(properties.getProperty("z1"));
    	publicKey[i] = TestUtils.hexToBytes(properties.getProperty("g2"));
    	proverIssuanceValues[i++] = TestUtils.hexToBytes(properties.getProperty("z2"));
    	publicKey[i] = TestUtils.hexToBytes(properties.getProperty("g3"));
    	proverIssuanceValues[i++] = TestUtils.hexToBytes(properties.getProperty("z3"));
    	publicKey[i] = TestUtils.hexToBytes(properties.getProperty("g4"));
    	proverIssuanceValues[i++] = TestUtils.hexToBytes(properties.getProperty("z4"));
    	publicKey[i] = TestUtils.hexToBytes(properties.getProperty("g5"));
    	proverIssuanceValues[i++] = TestUtils.hexToBytes(properties.getProperty("z5"));
    	publicKey[i] = TestUtils.hexToBytes(properties.getProperty("gt"));
    	proverIssuanceValues[i++] = TestUtils.hexToBytes(properties.getProperty("zt"));
    	if (device) {
    		publicKey[i] = TestUtils.hexToBytes(properties.getProperty("gd"));
    		proverIssuanceValues[i++] = TestUtils.hexToBytes(properties.getProperty("zd"));    	
    	}
    	ip.setPublicKey(publicKey);
    	ip.setProverIssuanceValues(proverIssuanceValues);
    	
    	byte[] encodingBytes = new byte[] {
    			Byte.valueOf(properties.getProperty("e1")).byteValue(),
    			Byte.valueOf(properties.getProperty("e2")).byteValue(),
    			Byte.valueOf(properties.getProperty("e3")).byteValue(),
    			Byte.valueOf(properties.getProperty("e4")).byteValue(),
    			Byte.valueOf(properties.getProperty("e5")).byteValue()
    	};
    	ip.setEncodingBytes(encodingBytes);
    	
    	
    	ip.setSpecification(TestUtils.hexToBytes(properties.getProperty("S")));
    	
    	return ip;
    }
    
    // tests the U-Prove Cryptographic Specification protocol test vectors
    public void testProtocols() throws IOException, InvalidProofException {
    	// load issuer parameters
    	IssuerParameters ip = loadIssuerParameters(false);
    	IssuerParametersInternal ipi = IssuerParametersInternal.generate(ip);
    	PrimeOrderGroup Gq = ip.getGroup(); 
    	FieldZq Zq = Gq.getZq();
    	ip.validate();
    	IssuerKeyAndParameters ikap = new IssuerKeyAndParameters(ip, TestUtils.hexToBytes(testVectors.getProperty("y0")));
    	
    	
    	/*
    	 *  token issuance
    	 */
    	
    	// load the shared data
    	byte[][] attributes = new byte[][] {
    			TestUtils.hexToBytes(testVectors.getProperty("A1")),
    			TestUtils.hexToBytes(testVectors.getProperty("A2")),
    			TestUtils.hexToBytes(testVectors.getProperty("A3")),
    			TestUtils.hexToBytes(testVectors.getProperty("A4")),
    			TestUtils.hexToBytes(testVectors.getProperty("A5"))
    	};
    	for (int i=1; i <= 5; i++) {
    		Assert.assertEquals(
    				Zq.getPositiveElement(TestUtils.hexToBytes(testVectors.getProperty("x"+i))), 
    				ProtocolHelper.computeXi(ipi, i, attributes[i-1]));
    	}
    	
    	byte[] tokenInformation = TestUtils.hexToBytes(testVectors.getProperty("TI"));
    	Assert.assertEquals(
    			Zq.getPositiveElement(TestUtils.hexToBytes(testVectors.getProperty("xt"))), 
    			ProtocolHelper.computeXt(ipi, tokenInformation));
    	

    	IssuerProtocolParameters issuerProtocolParams = new IssuerProtocolParameters();
    	issuerProtocolParams.setIssuerKeyAndParameters(ikap);
    	issuerProtocolParams.setNumberOfTokens(1);
    	issuerProtocolParams.setTokenAttributes(attributes);
    	issuerProtocolParams.setTokenInformation(tokenInformation);
    	IssuerImpl issuer = (IssuerImpl) issuerProtocolParams.generate();
    	issuer.precomputation(new byte[][] {TestUtils.hexToBytes(testVectors.getProperty("w"))});
    	byte[][] message1 = issuer.generateFirstMessage();
    	
    	ProverProtocolParameters proverProtocolParams = new ProverProtocolParameters();
    	proverProtocolParams.setIssuerParameters(ip);
    	proverProtocolParams.setNumberOfTokens(1);
    	proverProtocolParams.setTokenAttributes(attributes);
    	proverProtocolParams.setTokenInformation(tokenInformation);
    	proverProtocolParams.setProverInformation(TestUtils.hexToBytes(testVectors.getProperty("PI")));
    	ProverImpl prover = (ProverImpl) proverProtocolParams.generate();
    	prover.precomputation(
    			new byte[][] {TestUtils.hexToBytes(testVectors.getProperty("alpha"))},
    			new byte[][] {TestUtils.hexToBytes(testVectors.getProperty("beta1"))},
    			new byte[][] {TestUtils.hexToBytes(testVectors.getProperty("beta2"))});
    	byte[][] message2 = prover.generateSecondMessage(message1);
    	
    	byte[][] message3 = issuer.generateThirdMessage(message2);
    	
    	// issue token
    	UProveKeyAndToken[] upkt = prover.generateTokens(message3);
    	// validate token
    	Assert.assertNotNull(upkt[0]);
    	UProveToken upt = (UProveToken) upkt[0].getToken();
    	Assert.assertTrue(Arrays.equals(TestUtils.hexToBytes(testVectors.getProperty("UIDp")), upt.getIssuerParametersUID()));
    	Assert.assertTrue(Arrays.equals(TestUtils.hexToBytes(testVectors.getProperty("h")), upt.getPublicKey()));
    	Assert.assertTrue(Arrays.equals(TestUtils.hexToBytes(testVectors.getProperty("TI")), upt.getTokenInformation()));
    	Assert.assertTrue(Arrays.equals(TestUtils.hexToBytes(testVectors.getProperty("PI")), upt.getProverInformation()));
    	Assert.assertTrue(Arrays.equals(TestUtils.hexToBytes(testVectors.getProperty("sigma_z_prime")), upt.getSigmaZ()));
    	Assert.assertTrue(Arrays.equals(TestUtils.hexToBytes(testVectors.getProperty("sigma_c_prime")), upt.getSigmaC()));
    	Assert.assertTrue(Arrays.equals(TestUtils.hexToBytes(testVectors.getProperty("sigma_r_prime")), upt.getSigmaR()));
    	
    	/*
    	 * token presentation
    	 */
    	String[] D = testVectors.getProperty("D").split(",");
    	int[] disclosed = new int[D.length];
    	for (int i=0; i<D.length; i++) {
    		disclosed[i] = Integer.parseInt(D[i]);
    	}
    	byte[] m = TestUtils.hexToBytes(testVectors.getProperty("m"));    	
    	byte[][] w = new byte[][] {
    			TestUtils.hexToBytes(testVectors.getProperty("w0")),
    			TestUtils.hexToBytes(testVectors.getProperty("w1")),
    			TestUtils.hexToBytes(testVectors.getProperty("w3")),
    			TestUtils.hexToBytes(testVectors.getProperty("w4"))
    	};
		PresentationProof presentationProof = PresentationProtocol.generatePresentationProof(ip, disclosed, m, null, upkt[0], attributes, w);
    	Assert.assertTrue(Arrays.equals(
    			TestUtils.hexToBytes(testVectors.getProperty("a")), 
    			presentationProof.getA()));
    	Assert.assertTrue(Arrays.equals(
    			TestUtils.hexToBytes(testVectors.getProperty("r0")), 
    			presentationProof.getR0()));
    	Assert.assertTrue(Arrays.equals(
    			TestUtils.hexToBytes(testVectors.getProperty("r1")), 
    			presentationProof.getR()[0]));
    	Assert.assertTrue(Arrays.equals(
    			TestUtils.hexToBytes(testVectors.getProperty("r3")), 
    			presentationProof.getR()[1]));
    	Assert.assertTrue(Arrays.equals(
    			TestUtils.hexToBytes(testVectors.getProperty("r4")), 
    			presentationProof.getR()[2]));

		PresentationProtocol.verifyPresentationProof(ip, disclosed, m, null, upkt[0].getToken(), presentationProof);
    }

    // tests the U-Prove Cryptographic Specification protocol test vectors for Device
    public void testProtocolsDevice() throws IOException, InvalidProofException {
    	// load issuer parameters
    	IssuerParameters ip = loadIssuerParameters(true);
    	IssuerParametersInternal ipi = IssuerParametersInternal.generate(ip);
    	PrimeOrderGroup Gq = ip.getGroup(); 
    	FieldZq Zq = Gq.getZq();
    	ip.validate();
    	IssuerKeyAndParameters ikap = new IssuerKeyAndParameters(ip, TestUtils.hexToBytes(testVectors.getProperty("y0")));
    	
    	/*
    	 * Device setup
    	 */
		DeviceSetupParameters deviceSetupParams = new DeviceSetupParameters();
		deviceSetupParams.setIssuerParameters(ip);
		deviceSetupParams.setDevicePrivateKey(TestUtils.hexToBytes(testVectorsDevice.getProperty("xd")));
		Device device = deviceSetupParams.generate();
		int dIndex = ip.getProverIssuanceValues().length-1;
		byte[] zetad = device.GetDeviceParameter(ip.getProverIssuanceValues()[dIndex]); // TODO: create new method
		Assert.assertTrue(Arrays.equals(TestUtils.hexToBytes(testVectorsDevice.getProperty("zeta_d")), zetad));
		byte[] hd = device.GetDevicePublicKey();
		Assert.assertTrue(Arrays.equals(TestUtils.hexToBytes(testVectorsDevice.getProperty("hd")), hd));
    	
    	/*
    	 *  token issuance
    	 */

    	// load the shared data
    	byte[][] attributes = new byte[][] {
    			TestUtils.hexToBytes(testVectorsDevice.getProperty("A1")),
    			TestUtils.hexToBytes(testVectorsDevice.getProperty("A2")),
    			TestUtils.hexToBytes(testVectorsDevice.getProperty("A3")),
    			TestUtils.hexToBytes(testVectorsDevice.getProperty("A4")),
    			TestUtils.hexToBytes(testVectorsDevice.getProperty("A5"))
    	};
    	for (int i=1; i <= 5; i++) {
    		Assert.assertEquals(
    				Zq.getPositiveElement(TestUtils.hexToBytes(testVectorsDevice.getProperty("x"+i))), 
    				ProtocolHelper.computeXi(ipi, i, attributes[i-1]));
    	}
    	
    	byte[] tokenInformation = TestUtils.hexToBytes(testVectorsDevice.getProperty("TI"));
    	Assert.assertEquals(
    			Zq.getPositiveElement(TestUtils.hexToBytes(testVectorsDevice.getProperty("xt"))), 
    			ProtocolHelper.computeXt(ipi, tokenInformation));
    	

    	IssuerProtocolParameters issuerProtocolParams = new IssuerProtocolParameters();
    	issuerProtocolParams.setIssuerKeyAndParameters(ikap);
    	issuerProtocolParams.setNumberOfTokens(1);
    	issuerProtocolParams.setTokenAttributes(attributes);
    	issuerProtocolParams.setTokenInformation(tokenInformation);
    	issuerProtocolParams.setDevicePublicKey(hd);
    	IssuerImpl issuer = (IssuerImpl) issuerProtocolParams.generate();
    	issuer.precomputation(new byte[][] {TestUtils.hexToBytes(testVectorsDevice.getProperty("w"))});
    	byte[][] message1 = issuer.generateFirstMessage();
    	
    	ProverProtocolParameters proverProtocolParams = new ProverProtocolParameters();
    	proverProtocolParams.setIssuerParameters(ip);
    	proverProtocolParams.setNumberOfTokens(1);
    	proverProtocolParams.setTokenAttributes(attributes);
    	proverProtocolParams.setTokenInformation(tokenInformation);
    	proverProtocolParams.setProverInformation(TestUtils.hexToBytes(testVectorsDevice.getProperty("PI")));
    	proverProtocolParams.setDeviceParameters(hd, zetad);
    	ProverImpl prover = (ProverImpl) proverProtocolParams.generate();
    	prover.precomputation(
    			new byte[][] {TestUtils.hexToBytes(testVectorsDevice.getProperty("alpha"))},
    			new byte[][] {TestUtils.hexToBytes(testVectorsDevice.getProperty("beta1"))},
    			new byte[][] {TestUtils.hexToBytes(testVectorsDevice.getProperty("beta2"))});
    	byte[][] message2 = prover.generateSecondMessage(message1);
    	
    	byte[][] message3 = issuer.generateThirdMessage(message2);
    	
    	// issue token
    	UProveKeyAndToken[] upkt = prover.generateTokens(message3);
    	// validate token
    	Assert.assertNotNull(upkt[0]);
    	UProveToken upt = (UProveToken) upkt[0].getToken();
    	Assert.assertTrue(Arrays.equals(TestUtils.hexToBytes(testVectorsDevice.getProperty("UIDp")), upt.getIssuerParametersUID()));
    	Assert.assertTrue(Arrays.equals(TestUtils.hexToBytes(testVectorsDevice.getProperty("h")), upt.getPublicKey()));
    	Assert.assertTrue(Arrays.equals(TestUtils.hexToBytes(testVectorsDevice.getProperty("TI")), upt.getTokenInformation()));
    	Assert.assertTrue(Arrays.equals(TestUtils.hexToBytes(testVectorsDevice.getProperty("PI")), upt.getProverInformation()));
    	Assert.assertTrue(Arrays.equals(TestUtils.hexToBytes(testVectorsDevice.getProperty("sigma_z_prime")), upt.getSigmaZ()));
    	Assert.assertTrue(Arrays.equals(TestUtils.hexToBytes(testVectorsDevice.getProperty("sigma_c_prime")), upt.getSigmaC()));
    	Assert.assertTrue(Arrays.equals(TestUtils.hexToBytes(testVectorsDevice.getProperty("sigma_r_prime")), upt.getSigmaR()));
    	
    	/*
    	 * token presentation
    	 */
    	DeviceManager.RegisterDevice(device);
    	((DeviceImpl)device).setRandomizer(TestUtils.hexToBytes(testVectorsDevice.getProperty("wdPrime")));
    	String[] D = testVectorsDevice.getProperty("D").split(",");
    	int[] disclosed = new int[D.length];
    	for (int i=0; i<D.length; i++) {
    		disclosed[i] = Integer.parseInt(D[i]);
    	}
    	byte[] m = TestUtils.hexToBytes(testVectorsDevice.getProperty("m"));    	
    	byte[] md = TestUtils.hexToBytes(testVectorsDevice.getProperty("md"));    	
    	byte[][] w = new byte[][] {
    			TestUtils.hexToBytes(testVectorsDevice.getProperty("w0")),
    			TestUtils.hexToBytes(testVectorsDevice.getProperty("w1")),
    			TestUtils.hexToBytes(testVectorsDevice.getProperty("w3")),
    			TestUtils.hexToBytes(testVectorsDevice.getProperty("w4")),
    			TestUtils.hexToBytes(testVectorsDevice.getProperty("wd"))
    	};
		PresentationProof presentationProof = PresentationProtocol.generatePresentationProof(ip, disclosed, m, md, upkt[0], attributes, w);
    	Assert.assertTrue(Arrays.equals(
    			TestUtils.hexToBytes(testVectorsDevice.getProperty("a")), 
    			presentationProof.getA()));
    	Assert.assertTrue(Arrays.equals(
    			TestUtils.hexToBytes(testVectorsDevice.getProperty("r0")), 
    			presentationProof.getR0()));
    	Assert.assertTrue(Arrays.equals(
    			TestUtils.hexToBytes(testVectorsDevice.getProperty("r1")), 
    			presentationProof.getR()[0]));
    	Assert.assertTrue(Arrays.equals(
    			TestUtils.hexToBytes(testVectorsDevice.getProperty("r3")), 
    			presentationProof.getR()[1]));
    	Assert.assertTrue(Arrays.equals(
    			TestUtils.hexToBytes(testVectorsDevice.getProperty("r4")), 
    			presentationProof.getR()[2]));
    	Assert.assertTrue(Arrays.equals(
    			TestUtils.hexToBytes(testVectorsDevice.getProperty("rd")), 
    			presentationProof.getRd()));

		PresentationProtocol.verifyPresentationProof(ip, disclosed, m, md, upkt[0].getToken(), presentationProof);
    }


}
