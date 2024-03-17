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
package dev.luin.digikoppeling.gb.client.service;

import dev.luin.digikoppeling.gb.client.common.ExternalDataReferenceBuilder;
import dev.luin.file.client.core.file.FileId;
import dev.luin.file.client.core.file.FileSystem;
import io.vavr.collection.List;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import nl.logius.digikoppeling.gb._2010._10.ExternalDataReference;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class GBServiceImpl implements GBService
{
	@NonNull
	FileSystem fileSystem;
	@NonNull
	ExternalDataReferenceBuilder externalDataReferenceBuilder;

	@GET
	@Path("externalDataReference/{ids}")
	@Override
	public ExternalDataReference getExternalDataReference(@PathParam("ids") Long...ids) throws GBServiceException
	{
		log.debug("getExternalDataReference {}", (Object[])ids);
		val files = List.of(ids)
				.map(FileId::new)
				// .map(p -> fileService.getFile(path).orElseThrow(() -> new GBServiceException(p + " not found!")))
				.map(p -> fileSystem.findFile(p).<GBServiceException>getOrElseThrow(() -> new GBServiceException(p + " not found!")));
		return externalDataReferenceBuilder.build(files);
	}
}
