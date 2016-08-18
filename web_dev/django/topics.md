# Advanced Topics on Django

## Image Upload to model Imagefield

```python
# forms.py
class ImageUploadForm(forms.Form):
    """Image upload form."""
    image = forms.ImageField()


# models.py
class ExampleModel(models.Model):
    model_pic = models.ImageField(upload_to = 'pic_folder/', default = 'pic_folder/None/no-img.jpg')

```

Template

```html
<form action="{% url upload_pic %}" method="post" enctype="multipart/form-data">{% csrf_token %}
    <p>
        <input id="id_image" type="file" class="" name="image">
    </p>
    <input type="submit" value="Submit" />
</form>
```

The view

```python
def upload_pic(request):
    if request.method == 'POST':
        form = ImageUploadForm(request.POST, request.FILES)
        if form.is_valid():
            m = ExampleModel.objects.get(pk=course_id)
            m.model_pic = form.cleaned_data['image']
            m.save()
            return HttpResponse('image upload success')
    return HttpResponseForbidden('allowed only via POST')
```

Another example:

```python
import uuid, datetimefrom os.path import join, basename, splitext
image = models.ImageField(verbose_name=_(u'Image'), upload_to = tag_image_upload, blank = True, null=True)

def tag_image_upload(instance, filename):
    ext = filename.split('.')[-1]    
    filename = "%s.%s" % (uuid.uuid4(), ext)    
    return join('tags',  filename)
```
