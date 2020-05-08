from os import environ
from pybuilder.core import task, init
from ddadevops import *

name = 'webmeeting'
MODULE = 'jitsi-aws-instance'
PROJECT_ROOT_PATH = '../..'


class MyBuild(DdaSimpleMixin, DevopsTerraformBuild):
    pass


@init
def initialize(project):
    project.build_depends_on('ddadevops>=0.7.0.dev')
    stage = 'test'
    additional_vars = {"region": "eu-central-1", "path_to_ssh_pub": "/home/user/jitsi/.ssh/jitsi-test.pem"}
    config = create_devops_terraform_build_config(stage,
                                                  PROJECT_ROOT_PATH,
                                                  MODULE,
                                                  additional_vars,
                                                  use_workspace=False,
                                                  )
    config = add_dda_simple_mixin_config(config, MODULE + '_' + stage + '.edn',
                                         jar_file='target/uberjar/webmeeting-standalone.jar')
    build = MyBuild(project, config)
    build.initialize_build_dir()


@task
def plan(project):
    build = get_devops_build(project)
    build.plan()


@task
def tf_apply(project):
    build = get_devops_build(project)
    build.apply(True)


@task
def apply(project):
    build = get_devops_build(project)
    build.apply(True)
    out_json = build.read_output_json()
    write_target(build, out_json)
    write_domain(build, out_json)
    build.dda_install()


@task
def destroy(project):
    build = get_devops_build(project)
    build.destroy(True)


def write_target(build, out_json):
    build.dda_write_target(MODULE, out_json['ipv4']['value'])


def write_domain(build, out_json):
    build.dda_write_domain({'ipv4': out_json['ipv4']['value'],
                            'fqdn': out_json['fqdn']['value'],
                            })
