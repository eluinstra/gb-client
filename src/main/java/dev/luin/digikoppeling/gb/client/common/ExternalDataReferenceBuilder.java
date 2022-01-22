/*
 * Copyright 2020 E.Luinstra
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.luin.digikoppeling.gb.client.common;

import dev.luin.file.client.core.file.FSFile;
import dev.luin.file.client.core.file.Md5Checksum;
import dev.luin.file.client.core.file.Url;
import io.vavr.collection.Seq;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.val;
import lombok.experimental.FieldDefaults;
import nl.logius.digikoppeling.gb._2010._10.ChecksumType;
import nl.logius.digikoppeling.gb._2010._10.DataReference;
import nl.logius.digikoppeling.gb._2010._10.DataReference.Content;
import nl.logius.digikoppeling.gb._2010._10.DataReference.Transport;
import nl.logius.digikoppeling.gb._2010._10.DataReference.Transport.Location;
import nl.logius.digikoppeling.gb._2010._10.ExternalDataReference;
import nl.logius.digikoppeling.gb._2010._10.GbProfile;
import nl.logius.digikoppeling.gb._2010._10.UrlType;

@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class ExternalDataReferenceBuilder
{
	public ExternalDataReference build(Seq<FSFile> fsFiles)
	{
		val result = new ExternalDataReference();
		result.setProfile(GbProfile.DIGIKOPPELING_GB_1_0);
		result.getDataReference().addAll(fsFiles.map(this::createDataReference).asJava());
		return result;
	}

	private DataReference createDataReference(FSFile fsFile)
	{
		val result = new DataReference();
		result.setContextId(fsFile.getMd5Checksum().getValue());
		result.setContent(createContent(fsFile));
		result.setTransport(createTransport(fsFile.getUrl()));
		return result;
	}

	private Content createContent(FSFile fsFile)
	{
		val result = new Content();
		result.setFilename(fsFile.getName().getValue());
		result.setContentType(fsFile.getContentType().getValue());
		result.setSize(fsFile.getFileLength().toBigInteger());
		result.setChecksum(createMD5Checksum(fsFile.getMd5Checksum()));
		return result;
	}

	private ChecksumType createMD5Checksum(@NonNull Md5Checksum md5checksum)
	{
		val result = new ChecksumType();
		result.setType("MD5");
		result.setValue(md5checksum.getValue());
		return result;
	}

	private Transport createTransport(@NonNull Url url)
	{
		val result = new Transport();
		result.setLocation(createLocation(url));
		return result;
	}

	private Location createLocation(@NonNull Url url)
	{
		val result = new Location();
		result.setReceiverUrl(createUrl(url));
		return result;
	}

	private UrlType createUrl(@NonNull Url url)
	{
		val result = new UrlType();
		result.setType("xs:anyURI");
		result.setValue(url.getValue());
		return result;
	}

}
