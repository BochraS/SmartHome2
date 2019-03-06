/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CraTestModule } from '../../../test.module';
import { SmartObjectComponent } from 'app/entities/smart-object/smart-object.component';
import { SmartObjectService } from 'app/entities/smart-object/smart-object.service';
import { SmartObject } from 'app/shared/model/smart-object.model';

describe('Component Tests', () => {
    describe('SmartObject Management Component', () => {
        let comp: SmartObjectComponent;
        let fixture: ComponentFixture<SmartObjectComponent>;
        let service: SmartObjectService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CraTestModule],
                declarations: [SmartObjectComponent],
                providers: []
            })
                .overrideTemplate(SmartObjectComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmartObjectComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmartObjectService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new SmartObject(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.smartObjects[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
