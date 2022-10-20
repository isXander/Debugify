import re
import requests
import sys
import os

abspath = os.path.abspath(__file__)
dname = os.path.dirname(abspath)
os.chdir(dname)

version_list = requests.get("https://piston-meta.mojang.com/mc/game/version_manifest_v2.json").json()['versions']


def version_idx(version):
    return [(idx, ver) for idx, ver in enumerate(version_list) if ver['id'] == version]


with open('../gradle.properties', 'r') as propfile:
    gradleProperties = dict([map(lambda side: side.strip(), i.split('=', maxsplit=1)) for i in
                             filter(lambda line: len(line.strip()) > 2 and not line.lstrip().startswith('#'),
                                    propfile.readlines())])
minecraft_version = gradleProperties['minecraftVersion']
minecraft_version_idx = version_idx(minecraft_version)
print(f'Minecraft Version: {minecraft_version}')

with open('../PATCHED.md', 'r') as f:
    patched_lines = f.readlines()

bugs = []
start_unpatched = False
pattern = re.compile('\\|.+\\| \\[(MC-\\d+)]')

for line in patched_lines:
    if line.startswith('## '):
        if start_unpatched:
            break
        start_unpatched = line == '## Unpatched in vanilla\n'

    if not start_unpatched:
        continue

    match = re.search(pattern, line)
    if match is None:
        continue
    bugs.append(match.group(1))

resolved_count = 0
duplicate_count = 0
for bug in bugs:
    response = requests.get(f'https://bugs.mojang.com/rest/api/2/issue/{bug}')
    json_response = response.json()

    fields = json_response['fields']
    resolution_id = int(fields['resolution']['id']) if fields['resolution'] is not None else -1

    message_color = ''
    match resolution_id:
        case 1 | 5:  # resolved
            resolved_count += 1
            bug_status = "Resolved"
            message_color = '\033[91m'

            fix_versions = []
            for v in fields['fixVersions'] or []:
                fix_versions.append(v['name'].replace(' Pre-release ', '-pre').replace(' Release Candidate ', '-rc'))

            if len(fix_versions) > 0:
                bug_status += f' in {", ".join(fix_versions)}'

            if all(map(lambda v: version_idx(v) < minecraft_version_idx, fix_versions)) or any(
                    map(lambda v: v == "Future Update", fix_versions)):
                message_color = '\033[33m'
                resolved_count -= 1

        case 3:  # duplicate
            duplicate_count += 1
            bug_status = "Duplicate"
            message_color = '\033[33m'

            duplicates = None
            duplicate_fixed = False
            issue_links = fields['issuelinks']
            for issue in issue_links:
                if int(issue['type']['id']) == 10102:
                    duplicates = issue['outwardIssue']['key']
                    duplicate_fixed = int(issue['outwardIssue']['fields']['status']['id']) == 1 | 5
                    break

            if duplicates is not None:
                bug_status += f' of {duplicates}'
                if duplicate_fixed:
                    bug_status += f' which is resolved'
                    message_color = '\033[91m'
                    resolved_count += 1

        case _:
            bug_status = 'OK!'

    message = f"{message_color}{bug}: {bug_status}\033[0m"
    print(message, file=sys.stderr if resolution_id == 1 else sys.stdout)

if resolved_count == 0 and duplicate_count == 0:
    print('\nNothing to report!')
else:
    print()
    if duplicate_count > 0:
        print(f'\033[33m{duplicate_count} bug{"s have" if duplicate_count > 1 else " has"} been marked as duplicate!')
    if resolved_count > 0:
        print(f'\033[91m{resolved_count} bug{"s need" if resolved_count > 1 else " needs"} removing!')
        exit(-1)
