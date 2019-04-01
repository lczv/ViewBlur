# ViewBlur
A simple Android Module that uses RenderScript to apply a blur background on your Views

## How it works
Just call `(yourView).setBlurredBackground(radius)`, to make your View background transparent and apply the blur effect.

You can also uses the method `ViewBlur.applyBlur()` to retrieve a blurred version of a Bitmap.

## Requirements
Since it uses Kotlin features. It only works on kotlin projects a this time.
It also requires API >= 17 at this time.

### It's a very early build so it may not work as intended ###
