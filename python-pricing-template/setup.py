from setuptools import setup

setup(
    name='PythonPricingTemplate',
    version='0.1',
    description="Caplin Python Pricing Adapter Template",
    long_description=open('README.rst').read(),
    classifiers=[
        'Development Status :: 3 - Alpha',
        'Programming Language :: Python :: 3.6',
    ],
    url='https://github.com/caplin/project-templates',
    author='Caplin Systems Ltd',
    author_email="support@caplin.com",
    packages=['caplin.pricingtemplate'],
    install_requires=['caplin.datasource'],
    entry_points={
        'PricingTemplateAdapter': ['caplin-python-pricing-template=caplin.pricingtemplate.PricingTemplateAdapter:main'],
    },
    include_package_data=True
)
