/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype;

/**
 * Image implementation
 *
 * @author carcassi
 */
class IVImage extends VImage {

    private final int height;
    private final int width;
    private final byte[] data;

    public IVImage(int height, int width, byte[] data) {
        this.height = height;
        this.width = width;
        this.data = data;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public byte[] getData() {
        return data;
    }

}
