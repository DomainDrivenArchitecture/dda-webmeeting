from os import environ
from pybuilder.core import task, init
from ddadevops import *
import logging

name = 'webmeeting'
MODULE = 'jitsi-docker-image'
PROJECT_ROOT_PATH = '../..'

class MyBuild(DevopsDockerBuild):
    pass

@init
def initialize(project):
    project.build_depends_on('ddadevops>=0.6.0.dev4')
    stage = 'prod'
    dockerhub_user = gopass_field_from_path('meissa/web/docker.com', 'login')
    dockerhub_password = gopass_password_from_path("meissa/web/docker.com")
    config = create_devops_docker_build_config(
        stage, PROJECT_ROOT_PATH, MODULE, dockerhub_user, dockerhub_password)
    build = MyBuild(project, config)
    build.initialize_build_dir()


@task
def image(project):
    build = get_devops_build(project)
    build.image()

@task
def drun(project):
    build = get_devops_build(project)
    build.drun()

@task
def publish(project):
    build = get_devops_build(project)
    build.dockerhub_login()
    build.dockerhub_publish()

@task
def test(project):
    build = get_devops_build(project)
    build.test()
