/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
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
 ******************************************************************************/

package com.badlogic.gdx.graphics.g3d.model;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

/**
 * A BoneyKeyframe specifies the translation, kingdomRotation and scale of a frame within
 * a {@link NodeAnimation}.
 * @author badlogic
 *
 */
public class NodeKeyframe {
	/** the timestamp of this keyframe **/
	public float keytime;
	/** the translation, given in local space, relative to the parent **/
	public final Vector3 translation = new Vector3();
	/** the scale, given in local space relative to the parent **/
	public final Vector3 scale = new Vector3(1,1,1);
	/** the kingdomRotation, given in local space, relative to the parent **/
	public final Quaternion rotation = new Quaternion();
}