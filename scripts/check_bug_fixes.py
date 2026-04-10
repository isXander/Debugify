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

with open('../.bugs', 'r') as f:
    bugs_lines = f.readlines()

bugs = []
for line in bugs_lines:
    parts = line.split()
    if len(parts) >= 3 and parts[0] == 'patched':
        bugs.append((f'MC-{parts[1]}', parts[2]))

resolved_count = 0
duplicate_count = 0
for bug, side in bugs:
    try:
        response = requests.get(f'https://mojira.dev/api/v1/issues/{bug}')
    except Exception as e:
        print(f'Failed to fetch {bug}: {e}')
        continue

    if not response.ok:
        print(f'Failed to fetch {bug}: HTTP {response.status_code} {response.text.strip()}')
        continue

    try:
        json_response = response.json()
    except Exception as e:
        text = response.text.strip()
        if '\n' not in text and len(text) < 100:
            print(f'\033[35m{bug} ({side}): {text}\033[0m')
        else:
            print(f'Failed to parse response for {bug} ({side}): {e}\n{text}')
        continue

    resolution = json_response['resolution']
    fix_versions = [v.replace(' Pre-release ', '-pre').replace(' Release Candidate ', '-rc').replace(' Snapshot ', '-snapshot-').replace('Minecraft ', '')
                    for v in (json_response.get('fix_versions') or [])]

    message_color = ''
    match resolution:
        case 'Fixed' | "Won't Fix" | 'Works As Intended':
            resolved_count += 1
            bug_status = resolution
            message_color = '\033[91m'

            if len(fix_versions) > 0:
                bug_status += f' in {", ".join(fix_versions)}'

            if all(map(lambda v: version_idx(v) < minecraft_version_idx, fix_versions)) or any(
                    map(lambda v: v == "Future Update", fix_versions)):
                message_color = '\033[33m'
                resolved_count -= 1

        case 'Duplicate':
            duplicate_count += 1
            bug_status = "Duplicate"
            message_color = '\033[33m'

        case _:
            bug_status = 'OK!'

    is_resolved = resolution in ('Fixed', "Won't Fix", 'Works As Intended')
    message = f"{message_color}{bug} ({side}): {bug_status}\033[0m"
    print(message, file=sys.stderr if is_resolved else sys.stdout)

if resolved_count == 0 and duplicate_count == 0:
    print('\nNothing to report!')
else:
    print()
    if duplicate_count > 0:
        print(f'\033[33m{duplicate_count} bug{"s have" if duplicate_count > 1 else " has"} been marked as duplicate!')
    if resolved_count > 0:
        print(f'\033[91m{resolved_count} bug{"s need" if resolved_count > 1 else " needs"} removing!')
        exit(-1)
