#!/usr/bin/env python
# -*- coding: utf-8 -*-

import time

def tarai(x, y, z):
    if x <= y: return y
    return tarai(
        tarai(x - 1, y, z),
        tarai(y - 1, z, x),
        tarai(z - 1, x, y))

st_time = time.clock()
print(tarai(13, 5, 0))
print(time.clock() - st_time)
